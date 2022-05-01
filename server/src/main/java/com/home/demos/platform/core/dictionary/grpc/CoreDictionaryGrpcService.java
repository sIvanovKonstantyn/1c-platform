package com.home.demos.platform.core.dictionary.grpc;

import com.google.protobuf.Empty;
import com.home.demos.platform.common.dictionary.*;
import com.home.demos.platform.core.dictionary.service.CoreDictionaryService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@GrpcService
public class CoreDictionaryGrpcService extends CoreDictionaryServiceGrpc.CoreDictionaryServiceImplBase {

    @Autowired
    private CoreDictionaryService service;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public void save(CoreDictionaryRequest request, StreamObserver<CoreDictionaryResponse> responseObserver) {

        var response = CoreDictionaryResponse.newBuilder()
                .setData(buildCommonCoreDictionary(request, service.save(buildCoreDictionary(request))))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void update(CoreDictionaryRequest request, StreamObserver<CoreDictionaryResponse> responseObserver) {

        var response = CoreDictionaryResponse.newBuilder()
                .setData(buildCommonCoreDictionary(request, service.update(buildCoreDictionary(request))))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(Empty request, StreamObserver<CoreDictionaryMultipleResponse> responseObserver) {
        var response = CoreDictionaryMultipleResponse.newBuilder()
                .addAllData(
                        service.findAll().stream()
                                .map(c -> buildCommonCoreDictionary(CoreDictionaryRequest.getDefaultInstance(), c))
                                .collect(Collectors.toList())
                )
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void remove(CoreDictionaryRequest request, StreamObserver<CoreDictionaryResponse> responseObserver) {
        service.remove(buildCoreDictionary(request));
        responseObserver.onNext(CoreDictionaryResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    private com.home.demos.platform.core.dictionary.model.CoreDictionary buildCoreDictionary(CoreDictionaryRequest request) {
        return mapper.map(
                request.getData(),
                com.home.demos.platform.core.dictionary.model.CoreDictionary.class
        );
    }

    private CoreDictionary buildCommonCoreDictionary(CoreDictionaryRequest request, com.home.demos.platform.core.dictionary.model.CoreDictionary coreDictionary) {
        return request.getData().toBuilder()
                .setId(coreDictionary.getId())
                .setName(coreDictionary.getName())
                .setParentId(coreDictionary.getParentId())
                .addAllChildrenIds(coreDictionary.getChildrenIds())
                .build();
    }
}

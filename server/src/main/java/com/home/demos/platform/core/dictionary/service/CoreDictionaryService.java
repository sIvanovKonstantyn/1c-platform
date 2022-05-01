package com.home.demos.platform.core.dictionary.service;

import com.home.demos.platform.core.dictionary.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CoreDictionaryService {
    private static final List<CoreDictionary> repository = new ArrayList<>();

    public CoreDictionary save(CoreDictionary coreDictionary) {
        coreDictionary.setId(repository.size() + 1L);
        repository.add(coreDictionary);

        return coreDictionary;
    }

    public CoreDictionary update(CoreDictionary coreDictionary) {
        repository.stream()
                .filter(c -> c.getId().equals(coreDictionary.getId()))
                .forEach(
                        c -> {
                            c.setName(coreDictionary.getName());
                            c.setChildrenIds(coreDictionary.getChildrenIds());
                            c.setGroup(coreDictionary.getGroup());
                            c.setParentId(coreDictionary.getParentId());
                        }
                );

        return coreDictionary;
    }

    public List<CoreDictionary> findAll() {
        return repository;
    }

    public void remove(CoreDictionary coreDictionary) {
        repository.remove(coreDictionary);
    }
}

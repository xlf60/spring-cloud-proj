package com.xlf.restroom.converter;

import com.xlf.restroom.entity.ToiletEntity;
import com.xlf.restroom.pojo.Toilet;

public class ToiletConverter {

    public static Toilet convert(ToiletEntity entity) {

        return Toilet.builder()
                    .id(entity.getId())
                    .clean(entity.isClean())
                    .available(entity.isAvailable())
                    .build();
    }
}


package com.zzy.taofun.parse;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoParser;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.internal.mapping.Converter;
import com.taobao.api.internal.parser.json.JsonConverter;

public class LocalObjectJsonParser<T extends TaobaoResponse> implements TaobaoParser<T> {

    private Class<T> clazz;

    public LocalObjectJsonParser(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T parse(String rsp) throws ApiException {
        Converter converter = new LocalJsonConverter();
        return converter.toResponse(rsp, clazz);
    }

    public Class<T> getResponseClass() {
        return clazz;
    }
}

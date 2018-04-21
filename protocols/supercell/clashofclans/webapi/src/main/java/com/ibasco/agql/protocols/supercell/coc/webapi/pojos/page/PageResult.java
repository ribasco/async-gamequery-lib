package com.ibasco.agql.protocols.supercell.coc.webapi.pojos.page;

import com.google.gson.annotations.SerializedName;

public class PageResult<T> {
    @SerializedName("paging")
    private PageMeta pageMeta;
    private T items;

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public void setPageMeta(PageMeta pageMeta) {
        this.pageMeta = pageMeta;
    }

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }
}

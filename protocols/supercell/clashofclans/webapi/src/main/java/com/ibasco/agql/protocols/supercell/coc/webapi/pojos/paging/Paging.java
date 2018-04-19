package com.ibasco.agql.protocols.supercell.coc.webapi.pojos.paging;

public class Paging<T> {
    private PageMeta paging;
    private T items;

    public PageMeta getPaging() {
        return paging;
    }

    public void setPaging(PageMeta paging) {
        this.paging = paging;
    }

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }
}

package com.makernav.categorize.model;

import java.io.Serializable;
import java.util.Objects;

public class ProjetoItemId implements Serializable {
    private Integer projeto;
    private Integer item;
    
    public ProjetoItemId() {}
    
    public ProjetoItemId(Integer projeto, Integer item) {
        this.projeto = projeto;
        this.item = item;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjetoItemId that = (ProjetoItemId) o;
        return Objects.equals(projeto, that.projeto) && Objects.equals(item, that.item);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(projeto, item);
    }
}
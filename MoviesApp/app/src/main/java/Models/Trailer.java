package Models;

import java.io.Serializable;

/**
 * Created by alaa on 9/16/2016.
 */
public class Trailer implements Serializable {

    private String id;
    private String iso6391;
    private String key;
    private String name;
    private String site;
    private Integer size;
    private String type;

    public Trailer(String id, String iso_639_1, String key, String name, String site, String type) {
        this.id = id;
        this.iso6391 = iso_639_1;
        this.key = key;
        this.name = name;
        this.site = site ;
        this.type = site;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }


    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

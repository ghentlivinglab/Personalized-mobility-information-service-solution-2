/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.event;

import java.net.URL;

/**
 *
 * @author hannedesutter
 */
public class Source {
    private URL url;
    private String name;

    /**
     * The constructor for a Source.
     * @param url the url of the source.
     * @param name the name of the source.
     */
    public Source(URL url, String name) {
        this.url = url;
        this.name = name;
    }

    /**
     * The getter for the name of the source.
     * @return the name of the source.
     */
    public String getName() {
        return name;
    }

    /**
     * The getter for the url of the source.
     * @return the url of the source.
     */
    public URL getUrl() {
        return url;
    }

    
    
    
}

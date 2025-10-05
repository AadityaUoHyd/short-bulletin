package org.aadi.short_bulletin.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "news_items")
public class NewsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String summary;
    @ManyToOne
    private Bulletin bulletin;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public Bulletin getBulletin() { return bulletin; }
    public void setBulletin(Bulletin bulletin) { this.bulletin = bulletin; }
}
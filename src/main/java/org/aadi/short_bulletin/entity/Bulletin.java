package org.aadi.short_bulletin.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "bulletins")
public class Bulletin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    @OneToMany(cascade = CascadeType.ALL)
    private List<NewsItem> newsItems;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<NewsItem> getNewsItems() { return newsItems; }
    public void setNewsItems(List<NewsItem> newsItems) { this.newsItems = newsItems; }
}
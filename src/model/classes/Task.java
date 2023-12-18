package model.classes;

import model.enums.Category;
import model.enums.Priority;
import model.enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private int id;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private Status status;
    private LocalDate createdDate;
    private User user;
    private List<History> histories;

    public Task(String title, String description, Category category, Priority priority, Status status, LocalDate createdDate, User user, List<History> histories) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.createdDate = createdDate;
        this.user = user;
        this.histories = histories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", priority=" + priority +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", user=" + user +
                ", histories=" + histories +
                '}';
    }
}

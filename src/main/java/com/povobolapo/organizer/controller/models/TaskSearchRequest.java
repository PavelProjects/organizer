package com.povobolapo.organizer.controller.models;

public class TaskSearchRequest {
    private int page = 0;
    private int size = 10;
    private String sort = "ASC";
    private String login = null;
    private String status = null;

    public TaskSearchRequest() {
    }

    public TaskSearchRequest(int page, int size, String sort, String login, String status) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.login = login;
        this.status = status;
    }

    public TaskSearchRequest(String sort, String login, String status) {
        this.sort = sort;
        this.login = login;
        this.status = status;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskSearchRequest{" +
                "page=" + page +
                ", size=" + size +
                ", sort='" + sort + '\'' +
                ", login='" + login + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

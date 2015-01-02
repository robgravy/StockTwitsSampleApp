package sampleapp.stocktwits.com.stocksapp.Data;

public class Messages implements Comparable<Messages>{

    public String id;
    public String body;
    public Symbols[] symbols;
    public String created_at;
    public User user;

    @Override
    public int compareTo(Messages msg) {
        return msg.created_at.compareTo(created_at);
    }
}

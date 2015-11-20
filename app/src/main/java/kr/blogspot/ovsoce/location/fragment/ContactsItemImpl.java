package kr.blogspot.ovsoce.location.fragment;

/**
 * Created by jaeho_oh on 2015-11-20.
 */
public class ContactsItemImpl implements ContactsItem {
    private String name;
    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

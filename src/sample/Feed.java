package sample;

import javafx.scene.control.Label;

public class Feed {
    private String text = "";

    public boolean append(String string){
        this.text = "--" + string + "\n" + this.text;
        return true;
    }

    public boolean prepend(String string){
        this.text += string;
        return true;
    }

    @Override
    public String toString() {
        return this.text;
    }
}

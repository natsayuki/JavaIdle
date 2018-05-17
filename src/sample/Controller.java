package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

import java.util.*;

public class Controller {
    private int progress = 0;
    private int pamphlets = 10;
    private int material = 0;
    private int acquaintanceProgress = 0;
    private int followers = 0;
    private int followersMaking = 0;
    private int followersHanding = 0;
    private int followersCollecting = 0;
    private boolean inExpedition = false;
    private int expeditionTime = 0;
    private int expeditionMaxSize = 10;
    public int expeditionCurrSize = 10;
    private int expeditionSurvivors = 0;
    private boolean startExpedition = false;
    private boolean endExpedition = false;

    public HashMap<String, Integer> initExpeditionItemsCount(){
        HashMap<String, Integer> expeditionItemsCount = new HashMap<>();
        expeditionItemsCount.put("Wood", 0);
        expeditionItemsCount.put("Sand", 0);
        expeditionItemsCount.put("Gravel", 0);
        expeditionItemsCount.put("Stone", 0);
        expeditionItemsCount.put("Iron", 0);
        expeditionItemsCount.put("Gold", 0);
        expeditionItemsCount.put("Titanium", 0);
        return expeditionItemsCount;
    }

    HashMap<String, Integer> expeditionItemsCount = initExpeditionItemsCount();

    public HashMap<Integer, List<String>> initExpeditionItems() {
        HashMap<Integer, List<String>> expeditionItems = new HashMap<>();
        expeditionItems.put(1, Arrays.asList("Wood", "Sand"));
        expeditionItems.put(2, Arrays.asList("Gravel", "Stone"));
        expeditionItems.put(5, Arrays.asList("Iron"));
        expeditionItems.put(7, Arrays.asList("Gold"));
        expeditionItems.put(10, Arrays.asList("Titanium"));
        return expeditionItems;
    }
    HashMap<Integer, List<String>> expeditionItems = initExpeditionItems();


    @FXML
    private Button handPamphletButton;
    @FXML
    private Label pamphletCountLabel;
    @FXML
    private Label feedLabel;
    @FXML
    private Button acquaintanceButton;
    @FXML
    private Label followerCountLabel;
    @FXML
    private Label followerLabel;
    @FXML
    private TitledPane followersPane;
    @FXML
    private Label followersIdleCountLabel;
    @FXML
    private Label followersMakingCountLabel;
    @FXML
    private Label followersHandingCountLabel;
    @FXML
    private Button followersMakingAddButton;
    @FXML
    private Button followersMakingSubButton;
    @FXML
    private Button followersHandingAddButton;
    @FXML
    private Button followersHandingSubButton;
    @FXML
    private TitledPane expandPane;
    @FXML
    private Button expandMeetingBuildingButton;
    @FXML
    private TitledPane meetingBuildingPane;
    @FXML
    private Button callMeetingButton;
    @FXML
    private Label expandMeetingBuildingLabel;
    @FXML
    private HBox followersCollectingHBox;
    @FXML
    private Label followersCollectingCountLabel;
    @FXML
    private Button followersCollectingAddButton;
    @FXML
    private Button followersCollectingSubButton;
    @FXML
    private Label materialLabel;
    @FXML
    private Label materialCountLabel;
    @FXML
    private HBox expandHQHBox;
    @FXML
    private Button expandHQButton;
    @FXML
    private TitledPane HQPane;
    @FXML
    private Button sendExpeditionButton;
    @FXML
    private Label expeditionSizeLabel;
    @FXML
    private Button expeditionSizeAddButton;
    @FXML
    private Button expeditionSizeSubButton;
    @FXML
    private TitledPane expeditionItemsPane;
    @FXML
    private Label woodCountLabel;
    @FXML
    private Label sandCountLabel;
    @FXML
    private Label gravelCountLabel;
    @FXML
    private Label stoneCountLabel;
    @FXML
    private Label ironCountLabel;
    @FXML
    private Label goldCountLabel;
    @FXML
    private Label titaniumCountLabel;
    @FXML
    private HBox expandHousingHBox;
    @FXML
    private Button expandHousingButton;
    @FXML
    private HBox expandBusinessHBox;
    @FXML
    private Button expandBusinessButton;

    Feed feed = new Feed();
    Random rand = new Random();

    final String[] responses = {"I like the cause!", "Sounds like a good idea!"};

    public void initialize(){
        followersPane.setExpanded(false);
        expandPane.setExpanded(false);
        meetingBuildingPane.setExpanded(false);
        HQPane.setExpanded(false);
        expeditionItemsPane.setExpanded(false);
    }

    Runnable mainThread = new Runnable(){
        @Override
        public void run(){
            try{
                boolean running = true;
                while(running){
                    if(followersMaking > 0){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                pamphlets += followersMaking;
                                pamphletCountLabel.setText(String.valueOf(pamphlets));
                            }
                        });
                    }
                    if(followersHanding > 0){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                for(int i = 0; i<followersHanding; i++){
                                    if(pamphlets > 0){
                                        pamphlets--;
                                        if(rand.nextInt(3) + 1 == 1){
                                            handleFollowers();
                                        }
                                    }
                                }
                            }
                        });
                    }
                    if(followersCollecting > 0){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                material += followersCollecting;
                                materialCountLabel.setText(String.valueOf(material));
                            }
                        });
                    }
                    if(startExpedition){
                        feed.append("You send " + expeditionCurrSize + " followers off on an expedition.");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                feedLabel.setText(feed.toString());
                            }
                        });
                        followers -= expeditionCurrSize;
                        startExpedition = false;
                        inExpedition = true;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                sendExpeditionButton.setText("Recall Expedition");
                            }
                        });
                    }
                    if(endExpedition){
                        expeditionSurvivors = rand.nextInt(expeditionCurrSize) + 1;
                        feed.append("You call back your followers from their expedition.\nThere were " + expeditionSurvivors + " survivors.");
                        feed.append("They were out for " + expeditionTime + " days");
                        HashMap<String, Integer> recoveredItems = new HashMap<>();
                        for(Integer i: expeditionItems.keySet()){
                            if(rand.nextInt(i) +1 == i){
                                List<String> options = expeditionItems.get(i);
                                double amount = (double) expeditionTime / (double) i;
                                recoveredItems.put(options.get(rand.nextInt(options.size())), (int) (Math.ceil(amount) * Math.ceil(expeditionSurvivors / 2)));
                            }
                        }
                        for(String i: recoveredItems.keySet()){
                            int amount = recoveredItems.get(i);
                            feed.append("Recovered " + amount + " " + i);
                            expeditionItemsCount.put(i, expeditionItemsCount.get(i) + amount);
                        }
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                feedLabel.setText(feed.toString());
                                if(progress == 10) handleProgress();
                                for(String i: expeditionItemsCount.keySet()){
                                    int amount = expeditionItemsCount.get(i);
                                    if(i.equals("Wood")) woodCountLabel.setText(String.valueOf(amount));
                                    else if(i.equals("Sand")) sandCountLabel.setText(String.valueOf(amount));
                                    else if(i.equals("Gravel")) gravelCountLabel.setText(String.valueOf(amount));
                                    else if(i.equals("Stone")) stoneCountLabel.setText(String.valueOf(amount));
                                    else if(i.equals("Iron")) ironCountLabel.setText(String.valueOf(amount));
                                    else if(i.equals("Gold")) goldCountLabel.setText(String.valueOf(amount));
                                    else if(i.equals("Titanium")) titaniumCountLabel.setText(String.valueOf(amount));
                                }
                                sendExpeditionButton.setText("Send Expedition");
                            }
                        });
                        followers += expeditionSurvivors;
                        endExpedition = false;
                        inExpedition = false;
                        expeditionTime = 0;
                    }
                    if(inExpedition){
                        expeditionTime++;
                    }
                    Thread.sleep(1000);
                }
            }catch(InterruptedException event){

            }
        }
    };
    public boolean startThread(){
        new Thread(mainThread).start();
        return true;
    }
    boolean start = startThread();

    Runnable firstMeetingThread = new Runnable() {
        public void update(Feed feed){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    feedLabel.setText(feed.toString());
                }
            });
        }
        @Override
        public void run() {
            try{
                feed.append("Acquaintance:\nI'm glad you were all able to show up today.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Acquaintance:\nWe have been thinking about expanding the movement.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Acquaintance:\nAs you can see, we have already started with this cool meeting house.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Acquaintance:\nBut we plan on going further.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Acquaintance:\nWhat do you guys think?");
                update(feed);
                Thread.sleep(3000);
                feed.append("Follower 1:\nSounds good!");
                update(feed);
                Thread.sleep(3000);
                feed.append("Follower 2:\nI like that idea.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Follower 3:\nGo big or go home.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Acquaintance:\nAlright. Then it is decided.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Acquaintance:\nWe will start by collecting materials.");
                update(feed);
                Thread.sleep(3000);
                feed.append("Acquaintance:\nYou guys can help with that.");
                update(feed);
                handleProgress();
                Thread.sleep(3000);
                feed.append("Acquaintance:\nThe meeting is over.");
                update(feed);
                Thread.sleep(3000);
                
            }catch(InterruptedException e){

            }
        }
    };

    public boolean startFirstMeetingThread(){
        new Thread(firstMeetingThread).start();
        return true;
    }

    @FXML
    public void handleClick(ActionEvent e){
        if(e.getSource().equals(handPamphletButton)){
            pamphlets--;
            String text;
            if(pamphlets<=0) {
                pamphlets = 0;
                text = "You have no more pamphlets";
            }else if(pamphlets == 5 && progress == 0){
                text = "You hand out a pamphlet. \nThe person responds: \nOh hi there";
                handleProgress();
            }else{
                text = "You hand out a pamphlet. \nThe person looks uninterested";
                if(progress >= 2){
                    if(rand.nextInt(3) + 1 == 1){
                        if(progress == 2) handleProgress();
                        handleFollowers();
                        text = "You hand out a pamphlet. \nThe person responds:\n" + responses[rand.nextInt(responses.length)];
                    }
                }
            }
            pamphletCountLabel.setText(String.valueOf(pamphlets));
            feed.append(text);
            feedLabel.setText(feed.toString());
        }
        else if(e.getSource().equals(acquaintanceButton)){
            handleAcquaintance();
        }
        else if(e.getSource().equals(followersMakingAddButton)){
            if(followerRoleChange("making", 1)){
                feed.append("You asked one of your followers to help you make more pamphlets");
            }else{
                feed.append("You do not have enough followers to do that");
            }
            feedLabel.setText(feed.toString());
        }
        else if(e.getSource().equals(followersMakingSubButton)){
            if(followerRoleChange("making", -1)){
                feed.append("You asked one of your makers to stop");
            }else{
                feed.append("You do not have enough makers to do that");
            }
            feedLabel.setText(feed.toString());
        }
        else if(e.getSource().equals(followersHandingAddButton)){
            if(followerRoleChange("handing", 1)){
                feed.append("You asked one of your followers to help you hand out pamphlets");
            }else{
                feed.append("You do not have enough followers to do that");
            }
            feedLabel.setText(feed.toString());
        }
        else if(e.getSource().equals(followersHandingSubButton)){
            if(followerRoleChange("handing", -1)){
                feed.append("You asked one of your handers to stop");
            }else{
                feed.append("you do not have enough handers to do that");
            }
            feedLabel.setText(feed.toString());
        }
        else if(e.getSource().equals(followersCollectingAddButton)){
            if(followerRoleChange("collecting", 1)){
                feed.append("You asked one of your followers to help you collect material");
            }else{
                feed.append("You do not have enough followers to do that");
            }
            feedLabel.setText(feed.toString());
        }
        else if(e.getSource().equals(followersCollectingSubButton)){
            if(followerRoleChange("collecting", -1)){
                feed.append("You asked one of your collectors to stop");
            }else{
                feed.append("you do not have enough collectors to do that");
            }
            feedLabel.setText(feed.toString());
        }
        else if(e.getSource().equals(expandMeetingBuildingButton)){
            if(progress == 5){
                handleProgress();
            }
        }
        else if(e.getSource().equals(callMeetingButton)){
            callMeeting();
        }
        else if(e.getSource().equals(expandHQButton)){
            if(material >= 200){
                material -= 200;
                handleProgress();
            }else{
                feed.append("You don't have enough material to build that");
                feedLabel.setText(feed.toString());
            }
        }
        else if(e.getSource().equals(sendExpeditionButton)){
            if(progress == 9) handleProgress();
            if(inExpedition){
                endExpedition = true;
            }else{
                if(followers < expeditionCurrSize){
                    feed.append("You need at least " + expeditionCurrSize + " idle followers to start an expedition.");
                    feedLabel.setText(feed.toString());
                }else{
                    startExpedition = true;
                }
            }
        }
        else if(e.getSource().equals(expeditionSizeAddButton)){
            expeditionCurrSize++;
            if(expeditionCurrSize > expeditionMaxSize) expeditionCurrSize = expeditionMaxSize;
            expeditionSizeLabel.setText(String.valueOf(expeditionCurrSize));
        }
        else if(e.getSource().equals(expeditionSizeSubButton)){
            expeditionCurrSize--;
            if(expeditionCurrSize < 1) expeditionCurrSize = 1;
            expeditionSizeLabel.setText(String.valueOf(expeditionCurrSize));
        }
        else if(e.getSource().equals(expandHousingButton)){
            if(material > 5000 && expeditionItemsCount.get("Wood") > 100 && expeditionItemsCount.get("Stone") > 50){
                handleProgress();
            }
            else{
                feed.append("You need 5000 material, 100 wood, and 50 stone to build this.");
                feedLabel.setText(feed.toString());
            }
        }
        else if(e.getSource().equals(expandBusinessButton)){
            if(material > 50000 && expeditionItemsCount.get("Wood") > 1000 && expeditionItemsCount.get("Stone") > 500 &&
                    expeditionItemsCount.get("Gravel") > 500 && expeditionItemsCount.get("Sand") > 500 &&
                    expeditionItemsCount.get("Iron") > 150 && expeditionItemsCount.get("Gold") > 100 &&
                    expeditionItemsCount.get("Titanium") > 25){
                handleProgress();
            }
            else{
                feed.append("You need 50000 material, 1000 wood, 500 stone, 500 gravel, 500 sand, 150 iron, 100 gold, and 25 titanium to build this.");
                feedLabel.setText(feed.toString());
            }
        }
    }

    public void callMeeting(){
        switch(progress){
            case 6:
                handleProgress();
                break;
            default:
                feed.append("There is no reason to call a meeting");
                feedLabel.setText(feed.toString());
        }
    }

    public void handleProgress(){
        progress++;
        switch(progress){
            case 1:
                acquaintanceButton.getStyleClass().remove("hidden");
                break;
            case 3:
                followerLabel.getStyleClass().remove("hidden");
                followerCountLabel.getStyleClass().remove("hidden");
                break;
            case 4:
                followersPane.getStyleClass().remove("hidden");
                followersIdleCountLabel.setText(String.valueOf(followers));
                feed.append("Acquaintance:\nHey, we have 10 followers now!\nWhy don't you see if some of them want to help?");
                feedLabel.setText(feed.toString());
                break;
            case 5:
                expandPane.getStyleClass().remove("hidden");
                feed.append("Acquaintance:\nThis is wonderful!\nWe have reached 100 followers!\nThis may be the start of something great!");
                feedLabel.setText(feed.toString());
                break;
            case 6:
                feed.append("Acquaintance:\nA meeting building!\nWhat a great idea!\nLet's build it immediately");
                feedLabel.setText(feed.toString());
                meetingBuildingPane.getStyleClass().remove("hidden");
                expandMeetingBuildingButton.getStyleClass().add("hidden");
                expandMeetingBuildingLabel.getStyleClass().add("hidden");
                break;
            case 7:
                boolean firstMeetingStart = startFirstMeetingThread();
                break;
            case 8:
                followersCollectingHBox.getStyleClass().remove("hidden");
                materialLabel.getStyleClass().remove("hidden");
                materialCountLabel.getStyleClass().remove("hidden");
                expandHQHBox.getStyleClass().remove("hidden");
                break;
            case 9:
                feed.append("Acquaintance:\nOf course. A headquarters building.\nNow we can carry out all our operations.");
                feedLabel.setText(feed.toString());
                HQPane.getStyleClass().remove("hidden");
                expandHQHBox.getStyleClass().add("hidden");
                break;
            case 10:
                feed.append("Acquaintance:\nAlright. Our first expedition.\nHopefully everything goes well...");
                feedLabel.setText(feed.toString());
                break;
            case 11:
                feed.append("Acquaintance:\nThe expedition was successful!\nAnd look at this!\nThey brought back new materials!\nMore can join the expeditions if we give them a place to live.");
                feedLabel.setText(feed.toString());
                expeditionItemsPane.getStyleClass().remove("hidden");
                expandHousingHBox.getStyleClass().remove("hidden");
                break;
            case 12:
                feed.append("Acquaintance:\nYes the housing!\nNow we can send more people out!\nJust one thing.\nIf they are living here, they're gonna want to have something to do.");
                feedLabel.setText(feed.toString());
                expandBusinessHBox.getStyleClass().remove("hidden");
                expandHousingHBox.getStyleClass().add("hidden");
                expeditionMaxSize = 30;
                break;
            case 13:
                feed.append("Acquaintance:\nAlright! Business!\nNow more people will want to help!");
                feedLabel.setText(feed.toString());
                expandBusinessHBox.getStyleClass().add("hidden");
                expeditionMaxSize = 100;
        }
    }

    public void handleAcquaintance(){
        acquaintanceProgress++;
        switch(acquaintanceProgress){
            case 1:
                feed.append("Acquaintance:\nI like what you are doing.");
                break;
            case 2:
                feed.append("Acquaintance:\nHow about I help you out.");
                break;
            case 3:
                feed.append("Acquaintance:\nTalk to me and I'll help you make pamphlets");
                handleProgress();
                break;
            default:
                pamphlets++;
                pamphletCountLabel.setText(String.valueOf(pamphlets));
                feed.append("Acquaintance:\nHere you go");
        }
        feedLabel.setText(feed.toString());
    }

    public void handleFollowers(){
        followers++;
        followerCountLabel.setText(String.valueOf(followers + followersMaking + followersHanding));
        followersIdleCountLabel.setText(String.valueOf(followers));
        if(followers == 10 && progress == 3) handleProgress();
        if(followers + followersMaking + followersHanding > 100 && progress == 4) handleProgress();
    }
    public boolean followerRoleChange(String role, int amount){
        int followerRole = 0;
        if(role.equals("making")){
            followerRole = followersMaking;
        }else if(role.equals("handing")){
            followerRole = followersHanding;
        }else if(role.equals("collecting")){
            followerRole = followersCollecting;
        }
        int followerRoleInit = followerRole;
        if(amount > 0){
            if(followers > 0){
                followers--;
                followerRole++;
            }
        }if(amount < 0){
            if(followerRole > 0){
                followers++;
                followerRole--;
            }
        }
        if(role.equals("making")){
            followersMaking = followerRole;
            followersMakingCountLabel.setText(String.valueOf(followersMaking));
        }else if(role.equals("handing")){
            followersHanding = followerRole;
            followersHandingCountLabel.setText(String.valueOf(followersHanding));
        }else if(role.equals("collecting")){
            followersCollecting = followerRole;
            followersCollectingCountLabel.setText(String.valueOf(followersCollecting));
        }
        if(followerRole != followerRoleInit){
            followersIdleCountLabel.setText(String.valueOf(followers));
            return true;
        }
        return false;
    }
}

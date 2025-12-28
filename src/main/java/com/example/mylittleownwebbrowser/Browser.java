package com.example.mylittleownwebbrowser;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.Optional;

public class Browser extends BorderPane {
    private TabPane tabpane;
    private ProgressBar progressBar;
    private TextField inputpages;
    private Button goToPage;
    private Button opennewtabbutton;
    private Button goToStartpage;
    private Label pagestatus;
    private HBox undermenu;
    private MenuItem newTabItem;
    private MenuItem newWindow;
    private Settings settings;
    private MenuItem einstellung;
    private Menu hilfe;

    public Browser() {
        settings = new Settings("http://www.google.de");
        settings.loadfallconfigurations();
        initialUpperMenu();
        tabpane = new TabPane();
        setCenter(tabpane);
        initialunderMenu();
        registerListener();
        openNewTab();
    }

    private Tab getSelectedTab() {
        return tabpane.getSelectionModel().getSelectedItem();
    }

    private void ladeWebsite(String adresse) {
        adresse = checkWebsiteAddress(adresse);
        Tab selectedTab = getSelectedTab();
        selectedTab.setText(adresse);
        inputpages.setText(adresse);
        WebView view = (WebView) selectedTab.getContent();
        view.getEngine().load(adresse);
    }

    private void registerListener() {
        registerEnterListener();
        registergoToPageButtonListener();
        registerProgressstatusListener();
        registerOpenNewTabButtonListener();
        registergoToStartpageListener();
        registerOpenNewTabMenuListener();
        registerOpenNewWindowMenuListener();
        registerSettingsMenuListener();
        registerHelpMenuListener();
    }

    private void registerHelpMenuListener() {
        hilfe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("LittleFirefox");
                alert.setHeaderText("Gregory Mehlhorn");
                alert.setContentText("Testataufgabe zur Vorlesung OOP2" +
                                     "\nStudiengang: Informatik" +
                                     "\nStudiensemester: 2");

                alert.showAndWait();
            }
        });
    }

    private void registerSettingsMenuListener() {
        einstellung.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TextInputDialog dialog = new TextInputDialog(settings.getStartseite());
                dialog.setTitle("Alle Einstellungsmöglichkeiten");
                dialog.setHeaderText("Startseite");
                dialog.setContentText("Bitte die Startseite hier eintragen: ");
                Optional<String> ergebnis = dialog.showAndWait();
                if (ergebnis.isPresent()) {
                    settings.setStartseite(checkWebsiteAddress(ergebnis.get()));
                    settings.saveAllConfigurations();
                }
            }
        });
    }

    private void registerOpenNewWindowMenuListener() {
        newWindow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newWindows = new Stage();
                newWindows.setTitle("Mein eigener Webbrowser");
                newWindows.setScene(new Scene(new Browser()));
                newWindows.show();
            }
        });
    }

    private void registerOpenNewTabMenuListener() {
        newTabItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                openNewTab();
            }
        });
    }

    private void registerProgressstatusListener() {
        tabpane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {
                Tab selectedTab = getSelectedTab();
                if (selectedTab == null) {
                    return;
                }
                WebView view = (WebView) selectedTab.getContent();
                pagestatus.textProperty().bind(view.getEngine().getLoadWorker().messageProperty());
                progressBar.progressProperty().bind(view.getEngine().getLoadWorker().progressProperty());
                inputpages.setText(tabpane.getSelectionModel().getSelectedItem().getText());
            }
        });
    }

    private void registergoToStartpageListener() {
        goToStartpage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ladeWebsite(settings.getStartseite());
            }
        });
    }

    private void registerOpenNewTabButtonListener() {
        opennewtabbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                openNewTab();
            }
        });
    }

    private void registergoToPageButtonListener() {
        goToPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ladeWebsite(inputpages.getText());
            }
        });
    }

    private void registerEnterListener() {
        inputpages.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    ladeWebsite(inputpages.getText());
                }
            }
        });
    }

    private MenuBar initialSubMenu() {
        MenuBar submenu = new MenuBar();
        Menu datei = new Menu("Datei");
        newTabItem = new MenuItem("Neuer Tab");
        newWindow = new MenuItem("Neues Fenster");
        datei.getItems().addAll(newTabItem, newWindow);
        Menu extra = new Menu("Extras");
        einstellung = new MenuItem("Einstellungen");
        extra.getItems().add(einstellung);
        hilfe = new Menu("Hilfe");
        MenuItem ueber = new MenuItem("Über");
        hilfe.getItems().add(ueber);
        submenu.getMenus().addAll(datei, extra, hilfe);
        return submenu;
    }

    private void initialUpperMenu() {
        HBox upperMenu = new HBox();
        setTop(upperMenu);
        MenuBar menu = initialSubMenu();
        inputpages = new TextField(settings.getStartseite());
        inputpages.setPrefWidth(1000);
        Image opentab = new Image("https://cdn2.iconfinder.com/data/icons/ios-7-icons/50/plus-32.png");
        opennewtabbutton = new Button();
        opennewtabbutton.setGraphic(new ImageView(opentab));
        Image homepage = new Image("https://cdn0.iconfinder.com/data/icons/typicons-2/24/home-32.png");
        goToStartpage = new Button();
        goToStartpage.setGraphic(new ImageView(homepage));
        Image pageload = new Image("https://cdn3.iconfinder.com/data/icons/tango-icon-library/48/go-next-32.png");
        goToPage = new Button();
        goToPage.setGraphic(new ImageView(pageload));
        upperMenu.getChildren().addAll(menu, inputpages, opennewtabbutton, goToPage, goToStartpage);
    }

    private void initialunderMenu() {
        progressBar = new ProgressBar();
        pagestatus = new Label();
        undermenu = new HBox(progressBar, pagestatus);
        setBottom(undermenu);
    }

    public void openNewTab() {
        String urlPage = inputpages.getText();
        if (urlPage.isEmpty()) {
            urlPage = settings.getStartseite();
        }

        urlPage = checkWebsiteAddress(urlPage);
        WebView wv = new WebView();
        wv.getEngine().load(urlPage);
        Tab tab = new Tab(urlPage, wv);
        tabpane.getTabs().addAll(tab);
        tabpane.getSelectionModel().select(tab);
        registerNewWebview(wv);
        registerNewTab(tab);
    }

    private void registerNewTab(Tab tab) {
        tab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (tabpane.getTabs().isEmpty()) {
                    Platform.exit();
                }
            }
        });
    }

    public void registerNewWebview(WebView webView) {
        webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldValue, Worker.State newValue) {
                if (newValue == Worker.State.SUCCEEDED) {
                    undermenu.setVisible(false);
                } else if (newValue == Worker.State.SCHEDULED) {
                    undermenu.setVisible(true);
                } else if (newValue == Worker.State.FAILED) {
                    URL url = getClass().getResource("/FailedWebsite.html");
                    undermenu.setVisible(true);
                    webView.getEngine().load(url.toExternalForm());
                }
            }
        });
    }

    private String checkWebsiteAddress(String text) {
        if (!text.startsWith("https://")) {
            text = "http://" + text;
        }
        return text;
    }
}
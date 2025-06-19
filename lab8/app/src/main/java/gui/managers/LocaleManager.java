package gui.managers;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class LocaleManager {
    private static final LocaleManager INSTANCE = new LocaleManager();
    private static final String BUNDLE_PATH = "gui.i18n.messages";

    private final ObjectProperty<Locale> locale;
    private ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();

    private LocaleManager() {
        locale = new SimpleObjectProperty<>(new Locale("ru", "RU"));
        locale.addListener((observable, oldValue, newValue) -> loadResources(newValue));
        loadResources(locale.get());
    }

    public static LocaleManager getInstance() {
        return INSTANCE;
    }

    private void loadResources(Locale locale) {
        try {
            resources.set(ResourceBundle.getBundle(BUNDLE_PATH, locale));
        } catch (Exception e) {
            resources.set(ResourceBundle.getBundle(BUNDLE_PATH, Locale.ROOT));
            System.err.println("Could not find resource bundle for locale " + locale + ", using default.");
        }
    }

    public Locale getLocale() {
        return locale.get();
    }

    public void setLocale(Locale locale) {
        this.locale.set(locale);
    }

    public ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    public String getString(String key) {
        return resources.get().getString(key);
    }

    public StringBinding createStringBinding(String key) {
        return Bindings.createStringBinding(() -> getString(key), resources);
    }

    public String formatDate(LocalDate date) {
        if (date == null) return "";
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());
        return dtf.format(date);
    }

    public String formatNumber(Number number) {
        if (number == null) return "";
        NumberFormat nf = NumberFormat.getNumberInstance(getLocale());
        return nf.format(number);
    }
}
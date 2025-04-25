package utils;

import org.openqa.selenium.WebElement;

public class CustomElement {
    public static String getText(WebElement element) {
        return element.getText().trim();
    }
}

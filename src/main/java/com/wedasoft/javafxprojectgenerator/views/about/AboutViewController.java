package com.wedasoft.javafxprojectgenerator.views.about;


import com.wedasoft.javafxprojectgenerator.helper.HelperFunctions;

import java.io.IOException;
import java.net.URISyntaxException;

public class AboutViewController {

    public void init() {

    }

    public void onGithubHyperLinkClick() {
        try {
            HelperFunctions.openUrlInBrowser("https://github.com/davidweber411");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public void onHomepageHyperLinkClick() {
        try {
            HelperFunctions.openUrlInBrowser("https://wedasoft.com/");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

}

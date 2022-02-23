module Cliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.yaml.snakeyaml;
    requires lombok;
    requires io.vavr;
    requires retrofit2;
    requires okhttp3;
    requires retrofit2.converter.gson;
    requires com.google.gson;
    requires org.apache.logging.log4j;
    requires javafx.graphics;
    requires jakarta.enterprise.cdi.api;
    requires jakarta.inject.api;
    requires retrofit2.converter.scalars;
    requires io.reactivex.rxjava3;
    requires retrofit2.adapter.rxjava3;
    requires Common;
    requires org.pdfsam.rxjavafx;
    requires com.nimbusds.jose.jwt;
    requires org.bouncycastle.provider;
    requires com.google.common;


    opens quevedo.ClienteSecretos.gui;
    opens quevedo.ClienteSecretos.gui.controllers;
    opens quevedo.ClienteSecretos.gui.utils;
    opens quevedo.ClienteSecretos.config;
    opens quevedo.ClienteSecretos.service;
    opens quevedo.ClienteSecretos.dao;
    opens quevedo.ClienteSecretos.dao.retrofit;
    opens quevedo.ClienteSecretos.dao.utils;
    opens quevedo.ClienteSecretos.utils;
    opens quevedo.ClienteSecretos.dao.network;


    exports quevedo.ClienteSecretos.gui;
    exports quevedo.ClienteSecretos.dao;
    exports quevedo.ClienteSecretos.gui.controllers;
    exports quevedo.ClienteSecretos.gui.utils;
    exports quevedo.ClienteSecretos.config;
    exports quevedo.ClienteSecretos.service;
    exports quevedo.ClienteSecretos.dao.retrofit;
    exports quevedo.ClienteSecretos.dao.utils;
    exports quevedo.ClienteSecretos.utils;
    exports quevedo.ClienteSecretos.dao.network;


}
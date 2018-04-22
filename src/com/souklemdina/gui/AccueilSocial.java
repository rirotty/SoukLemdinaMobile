/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.souklemdina.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.OnOffSwitch;
import com.codename1.components.SpanLabel;
import com.codename1.ext.filechooser.FileChooser;
import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.souklemdina.entities.FosUser;
import com.souklemdina.entities.Post;
import com.souklemdina.entities.Profile;
import com.souklemdina.entities.PostHome;
import com.souklemdina.services.PostServices;
import com.souklemdina.services.ProfileServices;
import com.souklemdina.util.SessionUser;
import com.souklemdina.util.ImageViewerHerit;
import com.souklemdina.util.UploadFile;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rkader
 */
public class AccueilSocial {

    private Form f;
    private PostServices ps = new PostServices();
    private ProfileServices pr = new ProfileServices();
    private String newfilePath = "";

    private Label createForFont(Font fnt, String s) {
        Label l = new Label(s);
        l.getUnselectedStyle().setFont(fnt);
        return l;
    }

    public AccueilSocial() {
//        f = new Form("Hdith ElSouk", BoxLayout.y());
        f = new Form("Hdith ElSouk", new FlowLayout());
        Resources theme = UIManager.initFirstTheme("/theme");
        Font smallBoldSystemFont = Font.createTrueTypeFont("native:ItalicBold", "native:ItalicBold").derive(Display.getInstance().convertToPixels(3), Font.STYLE_PLAIN);
        Font smallLightSystemFont = Font.createTrueTypeFont("native:ItalicLight", "native:ItalicLight").derive(Display.getInstance().convertToPixels(3), Font.STYLE_PLAIN);
        //Here goes the connection Logic
        SessionUser.setUser(new FosUser(3));
        SessionUser.setProfile(new Profile(5));
        ArrayList<PostHome> arp = ps.getAccueil(3);
        Button btnAdd = new Button("Ajouter une publication");
        btnAdd.addActionListener((evt) -> {
            Form fa = new Form("Ajouter une publication", BoxLayout.y());
            Image icon = theme.getImage("back-command.png");
            icon = icon.scaled(70, 90);
            fa.getToolbar().addCommandToLeftBar("", icon, e -> {
                AccueilSocial acObj = new AccueilSocial();
                acObj.getF().showBack();
            });
            TextField tftitre = new TextField("", "Titre");
            fa.add(tftitre);
            TextField tftexte = new TextField("", "Texte");
            tftexte.setMaxSize(255);
            fa.add(tftexte);
            ImageViewer i = new ImageViewer();
            Container hcc = new Container(BoxLayout.y());
            Button btnOpen = new Button("Choisir Image");
            btnOpen.addActionListener((evt1) -> {
                ActionListener callback = e -> {
                    if (e != null && e.getSource() != null) {
                        try {
                            this.newfilePath = (String) e.getSource();
                            i.setImage(Image.createImage(this.newfilePath));
                            //Here goes the file upload logic
                            System.out.println(this.newfilePath);
                            try {
                                this.newfilePath = UploadFile.uploadImage(newfilePath, null);
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                            }
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                };
                if (FileChooser.isAvailable()) {
                    FileChooser.showOpenDialog(".jpg,image/jpg,.jpeg", callback);
                } else {
                    Display.getInstance().openGallery(callback, Display.GALLERY_IMAGE);
                }
            });
            Button btnConf = new Button("Publier");
            btnConf.addActionListener((evt1) -> {
                if (!this.newfilePath.equals("") && this.newfilePath != null) {
                    Post poss = new Post();
                    poss.setTexte(tftexte.getText());
                    poss.setTitre(tftitre.getText());
                    poss.setImage(this.newfilePath);
                    this.ps.addPost(poss, SessionUser.getUser().getId());
                    tftexte.clear();
                    tftitre.clear();
                    fa.add(new Label("Publication ajoutée avec succèes"));
                }
            });
            btnOpen.getStyle().setPadding(Component.LEFT, 10);
            btnOpen.getStyle().setPadding(Component.RIGHT, 10);
            btnConf.getStyle().setPadding(Component.LEFT, 10);
            btnConf.getStyle().setPadding(Component.RIGHT, 10);
            hcc.add(btnOpen);
            hcc.add(btnConf);
            fa.add(hcc);
            fa.add(i);
            fa.show();
        });
        Container vc = new Container(BoxLayout.y());
        btnAdd.getStyle().setPadding(Component.LEFT, 10);
        btnAdd.getStyle().setPadding(Component.RIGHT, 10);
        vc.add(btnAdd);
        f.add(vc);
        for (PostHome p : arp) {
            if (p.getPos().getImage() != null) {
                Image placeholder = Image.createImage(f.getWidth() / 3 - 4, f.getWidth() / 3 - 4, 0xbfc9d2);
                EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
                ImageViewerHerit img = new ImageViewerHerit(URLImage.createToStorage(encImage, "file" + p.getPos().getImage(),
                        "http://localhost/SoukLemdina/web/uploads/images/" + p.getPos().getImage()));
                Form fi = new Form(p.getPos().getTitre(), BoxLayout.y());
                Image icon = theme.getImage("back-command.png");
                icon = icon.scaled(70, 90);
                fi.getToolbar().addCommandToLeftBar("", icon, e -> {
                    AccueilSocial acObj = new AccueilSocial();
                    acObj.getF().showBack();
                });
                Image placeholderDet = Image.createImage(f.getWidth(), f.getWidth(), 0xbfc9d2);
                EncodedImage encImageDet = EncodedImage.createFromImage(placeholderDet, false);
                ImageViewer imgDet = new ImageViewer(URLImage.createToStorage(encImageDet, "fileDet" + p.getPos().getImage(),
                        "http://localhost/SoukLemdina/web/uploads/images/" + p.getPos().getImage()));
                fi.add(imgDet);
                Container hc = new Container(BoxLayout.x());
                hc.add(createForFont(smallLightSystemFont, p.getFirstname() + " " + p.getLastname()));
                if (p.getPr().getIdUser() != SessionUser.getUser().getId()) {
                    OnOffSwitch btff = new OnOffSwitch();
                    btff.setNoTextMode(true);
                    btff.setValue(p.getMfollowi());
                    btff.addActionListener((evt) -> {
                        pr.follow(SessionUser.getProfile().getId(), p.getPr().getId());
                    });
                    hc.add(btff);
                }
                fi.add(hc);
                SpanLabel titre = new SpanLabel(p.getPos().getTitre());
                fi.add(createForFont(smallBoldSystemFont, titre.getText()));
                SpanLabel texte = new SpanLabel(p.getPos().getTexte());
                texte.getTextAllStyles().setFont(smallLightSystemFont);
                fi.add(texte);

                img.setF(fi);
                f.add(img);
            }
        }
    }

    public Form getF() {
        return f;
    }

    public void setF(Form f) {
        this.f = f;
    }

}

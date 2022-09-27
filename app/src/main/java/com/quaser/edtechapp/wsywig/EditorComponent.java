package com.quaser.edtechapp.wsywig;

import android.view.View;

import com.quaser.edtechapp.wsywig.Components.ComponentsWrapper;
import com.quaser.edtechapp.wsywig.models.EditorContent;
import com.quaser.edtechapp.wsywig.models.EditorControl;
import com.quaser.edtechapp.wsywig.models.EditorType;
import com.quaser.edtechapp.wsywig.models.Node;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public abstract class EditorComponent {
    private final EditorCore editorCore;
    protected ComponentsWrapper componentsWrapper;
    public abstract Node getContent(View view);
    public abstract String getContentAsHTML(Node node, EditorContent content);
    public abstract void renderEditorFromState(Node node, EditorContent content);
    public abstract Node buildNodeFromHTML(Element element);
    public abstract void init(ComponentsWrapper componentsWrapper);

    public EditorComponent(EditorCore editorCore){
        this.editorCore = editorCore;
    }

    protected Node getNodeInstance(View view){
        Node node = new Node();
        EditorType type = editorCore.getControlType(view);
        node.type = type;
        node.content = new ArrayList<>();
        return node;
    }
    protected Node getNodeInstance(EditorType type){
        Node node = new Node();
        node.type = type;
        node.content = new ArrayList<>();
        return node;
    }

}
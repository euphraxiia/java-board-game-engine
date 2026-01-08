package tictactoe;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// Text field with placeholder that clears on focus
public class PlaceholderTextField extends JTextField {
    private final String placeholder;
    private boolean showingPlaceholder;
    
    public PlaceholderTextField(String placeholder, int columns) {
        super(columns);
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        setText(placeholder);
        setForeground(Color.GRAY);
        
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(Color.WHITE);
                    showingPlaceholder = false;
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (getText().trim().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY);
                    showingPlaceholder = true;
                }
            }
        });
        
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!showingPlaceholder) {
                    setForeground(Color.WHITE);
                }
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {}
            
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }
    
    @Override
    public String getText() {
        if (showingPlaceholder) {
            return "";
        }
        return super.getText();
    }
    
    public boolean isEmpty() {
        return getText().trim().isEmpty();
    }
}


package view.componentes;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * TextField con placeholder y resaltado de foco.
 */
public class PlaceholderTextField extends JTextField {
    private String placeholder;
    private boolean isPlaceholderShown = true;
    private final Color placeholderColor = new Color(149, 165, 166);
    private final Color textColor = new Color(44, 62, 80);
    private final Color borderColor = new Color(225, 232, 237);
    private final Color focusColor = new Color(74, 144, 226);

    public PlaceholderTextField(String placeholder) {
        this.placeholder = placeholder;
        configurarEstilo();
        configurarEventos();
    }

    private void configurarEstilo() {
        setFont(new Font("Arial", Font.PLAIN, 12));
        setPreferredSize(new Dimension(200, 35));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        setBackground(Color.WHITE);
        setText(placeholder);
        setForeground(placeholderColor);
        isPlaceholderShown = true;
    }

    private void configurarEventos() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isPlaceholderShown) {
                    setText("");
                    setForeground(textColor);
                    isPlaceholderShown = false;
                }
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(focusColor, 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(placeholderColor);
                    isPlaceholderShown = true;
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, 1),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                }
            }
        });

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isPlaceholderShown) {
                    setForeground(textColor);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isPlaceholderShown) {
                    setForeground(textColor);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    public String getTextReal() {
        return isPlaceholderShown ? "" : getText();
    }

    public void setError(boolean error) {
        if (error) {
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        } else {
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        }
    }

    public void setErrorWithTooltip(boolean error, String mensajeError) {
        setError(error);
        if (error) {
            setToolTipText(mensajeError);
        } else {
            setToolTipText(null);
        }
    }

    public void setTooltip(String mensaje) {
        if (mensaje != null && !mensaje.isEmpty()) {
            setToolTipText(mensaje);
        } else {
            setToolTipText(null);
        }
    }
}

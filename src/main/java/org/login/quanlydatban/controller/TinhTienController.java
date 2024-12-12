    package org.login.quanlydatban.controller;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Button;
    import javafx.scene.control.TextField;
    import javafx.scene.control.ToolBar;
    import javafx.stage.Stage;
    import org.login.quanlydatban.notification.Notification;
    import org.login.quanlydatban.utilities.NumberFormatter;

    import java.io.IOException;
    import java.net.URL;
    import java.util.*;

    public class TinhTienController implements Initializable {
        @FXML
        private ToolBar suggestionBox;

        @FXML
        private Button num0;

        @FXML
        private Button num1;

        @FXML
        private Button num2;

        @FXML
        private Button num3;

        @FXML
        private Button num4;

        @FXML
        private Button num5;

        @FXML
        private Button num6;

        @FXML
        private Button num7;

        @FXML
        private Button num8;

        @FXML
        private Button num9;

        @FXML
        private TextField tfTienGoc;

        @FXML
        private TextField tfTienKhachDua;

        @FXML
        private TextField tfTienTraLai;

        private TextField focusedField;

        @FXML
        private Button troVe;

        @FXML
        private Button xoa;

        private static final int[] MENH_GIA_TIEN = {1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000};


    //    private static TinhTienController instance;
    //
    //    private static Parent root;
    //
    //    public static TinhTienController getInstance() throws IOException {
    //        if(instance == null)
    //            instance = loadTienIch();
    //        return instance;
    //    }
    //
    //    private static TinhTienController loadTienIch() throws IOException {
    //        FXMLLoader loader = new FXMLLoader(ChonBanController.class.getResource("/org/login/quanlydatban/views/TrangTinhTien.fxml"));
    //        root = loader.load();
    //        return loader.getController();
    //    }
    //
    //    public Parent getRoot() {
    //        return root;
    //    }


        private TextField getFocusedTextField(TextField... textFields) {
            for (TextField textField : textFields) {
                if (textField.isFocused()) {
                    return textField;
                }
            }
            return null;
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            tfTienGoc.setOnMouseClicked(e -> focusedField = tfTienGoc);
            tfTienKhachDua.setOnMouseClicked(e -> focusedField = tfTienKhachDua);
            tfTienGoc.textProperty().addListener((observable, oldValue, newValue) -> {
                updateSuggestions(tfTienGoc);
                formatInputs(tfTienGoc);
                calculateChange();
            });
            tfTienKhachDua.textProperty().addListener((observable, oldValue, newValue) -> {
                updateSuggestions(tfTienKhachDua);
                formatInputs(tfTienKhachDua);
                calculateChange();
            });

        }




        @FXML
        void enter(ActionEvent event) {
    //        TextField focusedField = getFocusedTextField(tfTienGoc, tfTienKhachDua);
            if(focusedField != null){
                if(event.getSource().equals(num0))
                    focusedField.appendText("0");
                else if (event.getSource().equals(num1))
                    focusedField.appendText("1");
                else if (event.getSource().equals(num2))
                    focusedField.appendText("2");
                else if (event.getSource().equals(num3))
                    focusedField.appendText("3");
                else if (event.getSource().equals(num4))
                    focusedField.appendText("4");
                else if (event.getSource().equals(num5))
                    focusedField.appendText("5");
                else if (event.getSource().equals(num6))
                    focusedField.appendText("6");
                else if (event.getSource().equals(num7))
                    focusedField.appendText("7");
                else if (event.getSource().equals(num8))
                    focusedField.appendText("8");
                else if (event.getSource().equals(num9))
                    focusedField.appendText("9");
                else if (event.getSource().equals(xoa))
                    focusedField.clear();
                else
                    focusedField.setText(focusedField.getText().substring(0, focusedField.getLength() - 1));
            }
        }

        @FXML
        void xong(ActionEvent event) {
            Stage current = (Stage) tfTienGoc.getScene().getWindow();
            current.close();
        }

        @FXML
        void refresh(ActionEvent event) {
            focusedField = null;
            tfTienKhachDua.clear();
            tfTienGoc.clear();
            tfTienTraLai.clear();
        }

        @FXML
        private void updateSuggestions(TextField sourceField) {
            suggestionBox.getItems().clear(); // Xóa các gợi ý cũ
            String input = sourceField.getText().replace(".", "").trim();

            if (!input.isEmpty() && input.matches("\\d+")) { // Kiểm tra nếu nhập số
                int baseValue = Integer.parseInt(input);

                // Tạo danh sách các bội số
                int[] suggestionMultipliers = {1000, 10000, 100000, 1000000};

                for (int multiplier : suggestionMultipliers) {
                    long suggestion = (long) baseValue * multiplier;

                    // Kiểm tra nếu suggestion <= 100.000.000
                    if (suggestion <= 100_000_000) {
                        Button suggestionButton = new Button(NumberFormatter.formatPrice(String.valueOf(suggestion)));

                        // Add action to the suggestion button
                        suggestionButton.setOnAction(e -> {
                            sourceField.setText(NumberFormatter.formatPrice(String.valueOf(suggestion)));
                            sourceField.positionCaret(sourceField.getText().length());
                        });

                        suggestionBox.getItems().add(suggestionButton);
                    }
                }
            }
        }

        private void calculateChange() {
            try {
                // Lấy giá trị từ hai TextField và định dạng số tiền
                String tienGocStr = NumberFormatter.formatPrice(tfTienGoc.getText().replace(".", "").trim());
                String tienKhachDuaStr = NumberFormatter.formatPrice(tfTienKhachDua.getText().replace(".", "").trim());

                if (!tienGocStr.isEmpty() && !tienKhachDuaStr.isEmpty()) {
                    int tienGoc = Integer.parseInt(tienGocStr.replace(".", ""));
                    int tienKhachDua = Integer.parseInt(tienKhachDuaStr.replace(".", ""));

                    if (tienKhachDua >= tienGoc) {
                        int tienTraLai = tienKhachDua - tienGoc;
                        tfTienTraLai.setText(NumberFormatter.formatPrice(String.valueOf(tienTraLai)));
                    } else
                        tfTienTraLai.clear();
                }
            } catch (NumberFormatException e) {
                tfTienTraLai.clear();
            }
        }
        private void formatInputs(TextField textField) {
                String input = textField.getText().replace(".", "");
                int value = Integer.parseInt(input);
                textField.setText(NumberFormatter.formatPrice(String.valueOf(value)));

        }




















    }

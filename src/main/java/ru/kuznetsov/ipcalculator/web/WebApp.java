package ru.kuznetsov.ipcalculator.web;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import ru.kuznetsov.ipcalculator.services.IPCalculator;
import ru.kuznetsov.ipcalculator.services.InvalidIPFormatException;
import ru.kuznetsov.ipcalculator.services.Row;

import java.util.*;

@Route("app")
public class WebApp extends AppLayout {
    private VerticalLayout verticalLayout;
    private HorizontalLayout horizontalLayoutInput;
    private HorizontalLayout horizontalLayoutGrid;
    private Button button;
    private ComboBox comboBox;
    private TextField inputIp;
    private List<Row> rowList;
    private Grid<Row> grid;
    private IPCalculator ipCalculator;

    public WebApp() {
        verticalLayout = new VerticalLayout();
        horizontalLayoutInput = new HorizontalLayout();
        horizontalLayoutGrid = new HorizontalLayout();
        inputIp = new TextField();
        comboBox = new ComboBox();
        rowList = new ArrayList<>();
        ipCalculator = new IPCalculator();
        button = new Button();

        initItems();
        initLayouts();
    }

    private void initLayouts() {
        horizontalLayoutInput.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        horizontalLayoutInput.setAlignItems(FlexComponent.Alignment.END);
        horizontalLayoutGrid.setWidthFull();
        verticalLayout.add(horizontalLayoutInput, horizontalLayoutGrid);

        setContent(verticalLayout);
    }

    private void initItems() {
        initInputIp();
        initCombobox();
        initGrid();
        initButton();
    }

    private void initInputIp() {
        inputIp.setLabel("IP адрес (v4)");
        inputIp.setPlaceholder("178.248.87.211");
        inputIp.setAutofocus(true);
        horizontalLayoutInput.add(inputIp);
    }

    private void initCombobox() {
        comboBox.setItems(
                "\\0 - 0.0.0.0", "\\1 - 128.0.0.0", "\\2 - 192.0.0.0", "\\3 - 224.0.0.0", "\\4 - 240.0.0.0",
                "\\5 - 248.0.0.0", "\\6 - 252.0.0.0", "\\7 - 254.0.0.0", "\\8 - 255.0.0.0", "\\9 - 255.128.0.0",
                "\\10 - 255.192.0.0", "\\11 - 255.224.0.0", "\\12 - 255.240.0.0", "\\13 - 255.248.0.0", "\\14 - 255.252.0.0",
                "\\15 - 255.254.0.0", "\\16 - 255.255.0.0", "\\17 - 255.255.128.0", "\\18 - 255.255.192.0", "\\19 - 255.255.224.0",
                "\\20 - 255.255.240.0", "\\21 - 255.255.248.0", "\\22 - 255.255.252.0", "\\23 - 255.255.254.0", "\\24 - 255.255.255.0",
                "\\25 - 255.255.255.128", "\\26 - 255.255.255.192", "\\27 - 255.255.255.224", "\\28 - 255.255.255.240",
                "\\29 - 255.255.255.248", "\\30 - 255.255.255.252", "\\31 - 255.255.255.254", "\\32 - 255.255.255.255"
        );
        comboBox.setLabel("Маска (v4)");
        comboBox.setWidth("250px");

        horizontalLayoutInput.add(comboBox);
    }

    private void initButton() {
        button.setText("Посчитать");
        button.addClickListener(e -> {
            calculate();
        });

        horizontalLayoutInput.add(button);
    }

    private void initGrid() {
        if (grid == null) {
            grid = new Grid(Row.class);
            grid.removeColumnByKey("id");
            grid.setHeightByRows(true);
            grid.setWidthFull();
        } else {
            grid.removeAllColumns();
            grid.setItems(rowList);
            grid.setColumns("name", "decimalValue", "binaryValue", "hexDecimalValue");
            horizontalLayoutGrid.removeAll();
            horizontalLayoutGrid.add(grid);
        }

    }

    private void calculate() {
        Map<String, String[]> networkCharacteristics = null;

        if (inputIp.isEmpty()) {
            Notification.show("Введите ip!");
        } else {
            try {
                rowList.clear();
                networkCharacteristics = ipCalculator.calculateNetworkCharacteristics(inputIp.getValue(), comboBox.getValue().toString());
                networkCharacteristics.entrySet().stream().forEach(e -> rowList.add(new Row(e.getKey(), e.getValue())));
                initGrid();
            } catch (InvalidIPFormatException e) {
                Notification.show(e.getMessage());
            }
        }
    }
}

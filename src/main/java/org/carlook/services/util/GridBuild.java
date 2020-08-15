package org.carlook.services.util;

import com.vaadin.ui.Grid;
import org.carlook.model.objects.entities.Auto;

import java.util.List;

public class GridBuild {

    private GridBuild(){}

    public static Grid<Auto> basicGrid(List<Auto> autos){
        Grid<Auto> autoGrid = new Grid<>();
        autoGrid.setSizeUndefined();
        autoGrid.removeAllColumns();
        autoGrid.setCaptionAsHtml(true);
        autoGrid.setItems(autos);
        autoGrid.setHeightByRows(!autos.isEmpty() ? autos.size() : 1);
        autoGrid.setWidth("100%");

        autoGrid.addColumn(Auto::getMarke).setCaption("Automarke").setWidth(230);
        autoGrid.addColumn(Auto::getBaujahr).setCaption("Baujahr").setWidth(90);
        autoGrid.addColumn(Auto::getBeschreibung).setCaption("Beschreibung");

        return autoGrid;
    }
}

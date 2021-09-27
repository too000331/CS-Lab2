import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

public class CustomItemModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private Policy policy;
    private List<CustomItem> items;

    private String[] columns = new String[] {"selected", "description", "solution", "info", "see_also", "type", "value_type",
            "value_data", "right_type", "reg_item", "reg_key", "reference"};

    public CustomItemModel(Policy policy) {
        setPolicy(policy);
    }

    @Override
    public int getRowCount() {
        return policy != null ? this.items.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(policy == null) {
            return null;
        }

        CustomItem item = this.items.get(rowIndex);
        if(columnIndex == 0) {
            return item.isSelected();
        } else {
            String column = columns[columnIndex];
            if(item.getJson().has(column)){
                String strVal = item.getJson().getString(column);
                return strVal.replace("\\n", "\n");
            } else {
                return null;
            }
        }
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
        if(policy != null) {
            this.items = policy.getItems();
        }
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 0) {
            return Boolean.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            CustomItem item = this.items.get(rowIndex);
            item.setSelected((Boolean)aValue);
        }
    }

    public void filter(String searchString) {
        this.items = policy.getItems().stream()
                .filter(item -> {
                    for(String key : item.getJson().keySet()) {
                        if(item.getJson().getString(key).toLowerCase().contains(searchString.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
        fireTableDataChanged();
    }

}

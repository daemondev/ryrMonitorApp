package com.example.eidan.wsapp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class AgentTableDataAdapter extends LongPressAwareTableDataAdapter<Agent> {
    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();

    public AgentTableDataAdapter(Context context, final List<Agent> data, final TableView<Agent> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Agent agent = getRowData(columnIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderCatName(agent);
                break;
            case 1:
                renderedView = renderCatName(agent);
                break;
            case 2:
                renderedView = renderAgentState(agent, parentView);
                break;
            case 3:
                renderedView = renderCatName(agent);
                break;
        }

        return renderedView;
    }

    private View renderCatName(final Agent agent) {
        return renderString(agent.getExten());
    }

    private View renderAgentState(final Agent car, final ViewGroup parentView) {
        final View view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(car.getState());
        return view;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Agent agent = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableCatName(agent);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private View renderEditableCatName(final Agent agent) {
        final EditText editText = new EditText(getContext());
        editText.setText(agent.getCallerid());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new AgentUpdater(agent));
        return editText;
    }

    private static class AgentUpdater implements TextWatcher {

        private Agent agentToUpdate;

        public AgentUpdater(Agent agentToUpdate) {
            this.agentToUpdate = agentToUpdate;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            agentToUpdate.setCallerid(Integer.parseInt(s.toString()));
        }
    }
}
package com.op.eschool.adapters;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.op.eschool.R;
import com.op.eschool.models.parents_model.ParentModel;

import java.util.ArrayList;
import java.util.List;

public class ParentNexusAdapter extends BaseTableAdapter {
    Activity activity ;
    Context context ;
    private final NexusTypes familys[];
    private final String headers[] = {
            "ID",
            "Photo",
            "Name",
            "Gender",
            "Occupation",
            "Address",
            "Mobile No",
            "Alternate Mobile No",
            "Email"

    };
    private final int[] widths = {
            100,
            100,
            150,
            60,
            100,
            100,
            100,
            100,
            100

    };
    private final float density;

    public ParentNexusAdapter(Activity activity , Context context , List<ParentModel> list) {
        this.activity = activity ;
        this.context = context ;
        familys = new NexusTypes[] {
                new NexusTypes(""),
                new NexusTypes(""),
                new NexusTypes(""),
        };
        density = context.getResources().getDisplayMetrics().density;

        for (ParentModel parentModel : list){
            familys[0].list.add(new Nexus(""+parentModel.getParentId(),
                    ""+parentModel.getPrtPhoto(),
                    ""+parentModel.getPrtName(),
                    ""+parentModel.getPrtGender(),
                    "",
                    ""+parentModel.getPrtAddress(),
                    ""+parentModel.getPrtMobile(),
                    "",
                    "")) ;

        }

        familys[0].list.add(new Nexus("","","","","","","","","")) ;
        familys[1].list.add(new Nexus("","","","","","","","","")) ;
        familys[1].list.add(new Nexus("","","","","","","","","")) ;
        familys[1].list.add(new Nexus("","","","","","","","","")) ;
        familys[1].list.add(new Nexus("","","","","","","","","")) ;
        familys[2].list.add(new Nexus("","","","","","","","","")) ;

        }

    @Override
    public int getRowCount() {
        return familys[0].list.size() ;
    }

    @Override
    public int getColumnCount() {
        return headers.length -1;
    }

    @Override
    public View getView(int row, int column, View convertView, ViewGroup parent) {
        final View view;
        Log.w("getView", "getItemViewType: >>"+ getItemViewType(row, column));
        switch (getItemViewType(row, column)) {
            case 0:
                view = getFirstHeader(row, column, convertView, parent);
                break;
            case 1:
                view = getHeader(row, column, convertView, parent);
                break;
            case 2:
                view = getFirstBody(row, column, convertView, parent);
                break;
            case 3:
                view = getBody(row, column, convertView, parent);
                break;
            case 4:
                view = getFamilyView(row, column, convertView, parent);
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return view;
    }

    private View getFirstHeader(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_header_first, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(headers[0]);
        return convertView;
    }

    private View getHeader(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_header, parent, false);
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(headers[column + 1]);
        return convertView;
    }

    private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_first, parent, false);
        }
        convertView.setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1 : R.drawable.bg_table_color2);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column + 1]);
        return convertView;
    }

    private View getBody(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table, parent, false);
        }
        convertView.setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1 : R.drawable.bg_table_color2);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column + 1]);

        return convertView;
    }

    private View getFamilyView(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_table_family, parent, false);
        }
        final String string;
        if (column == -1) {
            string = getFamily(row).name;
        } else {
            string = "";
        }
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(string);
        return convertView;
    }

    @Override
    public int getWidth(int column) {
        return Math.round(widths[column + 1] * density);
    }

    @Override
    public int getHeight(int row) {
        final int height;
        if (row == -1) {
            height = 35;
        } else if (isFamily(row)) {
            height = 25;
        } else {
            height = 45;
        }
        return Math.round(height * density);
    }

    @Override
    public int getItemViewType(int row, int column) {
        final int itemViewType;
        if (row == -1 && column == -1) {
            itemViewType = 0;
        } else if (row == -1) {
            itemViewType = 1;
        } else if (isFamily(row)) {
            itemViewType = 4;
        } else if (column == -1) {
            itemViewType = 2;
        } else {
            itemViewType = 3;
        }
        return itemViewType;
    }

    private boolean isFamily(int row) {
        int family = 0;
        while (row > 0) {
            row -= familys[family].size() + 1;
            family++;
        }
        return row == 0;
    }

    private NexusTypes getFamily(int row) {
        int family = 0;
        while (row >= 0) {
            row -= familys[family].size() + 1;
            family++;
        }
        return familys[family - 1];
    }

    private Nexus getDevice(int row) {
        int family = 0;
        while (row >= 0) {
            row -= familys[family].size() + 1;
            family++;
        }
        family--;
        return familys[family].get(row + familys[family].size());
    }

    @Override
    public int getViewTypeCount() {
        return headers.length -1;
    }
    private class NexusTypes {
        private final String name;
        private final List<Nexus> list;

        NexusTypes(String name) {
            this.name = name;
            list = new ArrayList<Nexus>();
        }

        public int size() {
            return list.size();
        }

        public Nexus get(int i) {
            return list.get(i);
        }
    }

    private class Nexus {
        private final String[] data;

        private Nexus(
                String ID,
                String Photo,
                String Name,
                String Gender,
                String Occupation,
                String Address,
                String Mobile_No,
                String Alternate_Mobile_No,
                String Email) {

            data = new String[] {
                    ID,
                    Photo,
                    Name,
                    Gender,
                    Occupation,
                    Address,
                    Mobile_No,
                    Alternate_Mobile_No,
                    Email
            };
        }
    }


}
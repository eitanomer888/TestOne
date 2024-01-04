package com.example.pretest1retest;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Path;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TableLayout tbList;
    private MyDatabaseHelper myDatabaseHelper;

    private ArrayList<TextView> tvs1 = new ArrayList<>();
    private ArrayList<TextView> tvs2 = new ArrayList<>();
    private ArrayList<TextView> tvs3 = new ArrayList<>();

    private ArrayList<Button> btnsD = new ArrayList<>();
    private ArrayList<Button> btnsU = new ArrayList<>();
    private ArrayList<TableRow> tbRows = new ArrayList<>();
    private View view;
    private Cursor cursor;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);


        tbList = view.findViewById(R.id.tbList);
        myDatabaseHelper = new MyDatabaseHelper(getActivity());

        cursor = myDatabaseHelper.readAllData();
        if(cursor == null){
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int n = cursor.getCount();

            cursor.moveToFirst();

            for (int i = 0; i < n; i++) {

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String product = cursor.getString(2);
                int buy = cursor.getInt(3);


                AddRow(name,product,buy,id);

                cursor.moveToNext();
            }
        }










        return  view;
    }



    void AddRow(String name,String product,int buy,int id)
    {
        TableRow tb = new TableRow(getActivity());
        tb.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tb.setTag(id);

        TextView tv1 = new TextView(getActivity());
        tv1.setText(name);
        tv1.setWidth(170);
        tvs1.add(tv1);

        TextView tv2 = new TextView(getActivity());
        tv2.setText(product);
        tv2.setWidth(170);
        tv2.setPadding(70,0,30,0);
        tvs2.add(tv2);

        TextView tv3 = new TextView(getActivity());
        tv3.setText("נקנה");
        tv3.setWidth(170);
        tvs3.add(tv3);
        if(buy == 0)
        {
            tv3.setText("לא נקנה");
        }

        Button btnDel = new Button(getActivity());
        btnDel.setText("מחק");
        btnsD.add(btnDel);
        btnDel.setOnClickListener(this);

        Button btnUp = new Button(getActivity());
        btnUp.setText("עדכן");
        btnsU.add(btnUp);
        btnUp.setOnClickListener(this);

        tb.addView(tv1);
        tb.addView(tv2);
        tb.addView(tv3);
        tb.addView(btnDel);
        tb.addView(btnUp);


        tbRows.add(tb);

        tbList.addView(tb);
    }

    void ShowDialog(int i)
    {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        EditText etNameD = dialog.findViewById(R.id.etNameD);
        EditText etProductD = dialog.findViewById(R.id.etProductD);
        CheckBox cbBuyD = dialog.findViewById(R.id.cbBuyD);
        Button btnAddD = dialog.findViewById(R.id.btnAddD);

        String name = tvs1.get(i).getText().toString();
        String product = tvs2.get(i).getText().toString();
        String buy = tvs3.get(i).getText().toString();


        etNameD.setText(name);
        etProductD.setText(product);
        if(buy.equals("נקנה"))
        {
            cbBuyD.setChecked(true);
        }

        btnAddD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = etNameD.getText().toString();
                String product = etProductD.getText().toString();
                String buy = "לא נקנה";
                String b = "0";
                if(cbBuyD.isChecked()){
                    buy = "נקנה";
                    b = "1";
                }

                tvs1.get(i).setText(name);
                tvs2.get(i).setText(product);
                tvs3.get(i).setText(buy);



                myDatabaseHelper.updateData(tbRows.get(i).getTag().toString(),name,product,b);

                dialog.dismiss();
            }
        });




        dialog.show();
    }

    @Override
    public void onClick(View v)
    {
        for (int i = 0; i < btnsD.size(); i++)
        {
            if(v == btnsD.get(i))
            {
                //מחיקה מDB
                myDatabaseHelper.deleteOneRow(tbRows.get(i).getTag().toString());

                //מחיקת שורה מTABLE
                tbList.removeView(tbRows.get(i));
            }
        }

        for (int i = 0; i < btnsU.size(); i++)
        {
            if(v == btnsU.get(i)){

                ShowDialog(i);


            }
        }
    }
}
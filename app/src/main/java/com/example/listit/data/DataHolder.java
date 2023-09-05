package com.example.listit.data;

import com.example.listit.adapters.DatabaseAdapter;

import java.util.List;

public class DataHolder {

    public static List<TaskModel> tasks = DatabaseAdapter.getAllData();

}

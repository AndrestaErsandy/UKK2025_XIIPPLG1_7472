package com.example.ukk2025_xiipplg1;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>  {

    private List<CategoryModel> categoryList;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryModel> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categoryList.get(position);
        holder.textNomor.setText(String.valueOf(position + 1));
        holder.textCategory.setText(category.getNama());

        // DELETE ACTION
        holder.deleteIcon.setOnClickListener(v -> showDeleteConfirmationDialog(category, position));

        // EDIT ACTION
        holder.editIcon.setOnClickListener(v -> showEditDialog(category, position));
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textNomor, textCategory;
        ImageView deleteIcon, editIcon;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomor = itemView.findViewById(R.id.textNomor);
            textCategory = itemView.findViewById(R.id.textCategory);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }

    private void showDeleteConfirmationDialog(CategoryModel category, int position) {
        new AlertDialog.Builder(context)
                .setMessage("Apakah Anda yakin ingin menghapus category ini?")
                .setCancelable(false)
                .setPositiveButton("Yakin", (dialog, id) -> deleteCategory(category, position))
                .setNegativeButton("Batal", (dialog, id) -> dialog.dismiss())
                .create()
                .show();
    }

    private void deleteCategory(CategoryModel category, int position) {
        String url = "http://172.16.0.106/ukk2025/hapusKat.php?id=" + category.getId();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Toast.makeText(context, "category dihapus!", Toast.LENGTH_SHORT).show();
                    categoryList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, categoryList.size());
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Gagal menghapus category! Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }


    private void showEditDialog(CategoryModel category, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_edt_category, null);
        builder.setView(view);

        EditText editNama = view.findViewById(R.id.editNama);

        editNama.setText(category.getNama());

        builder.setTitle("Edit Category")
                .setPositiveButton("Simpan", (dialog, which) -> {
                    updateCategory(category.getId(), editNama.getText().toString());
                    dialog.dismiss(); // Tutup dialog setelah menyimpan
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void updateCategory(int id, String nama) {
        String url = "http://172.16.0.106/ukk2025/updateKat.php";

        JSONObject params = new JSONObject();
        try {
            params.put("id", id);
            params.put("nama", nama);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(context, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }

}

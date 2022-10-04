package com.quaser.edtechapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quaser.edtechapp.R;
import com.quaser.edtechapp.models.PaymentModel;
import com.quaser.edtechapp.rest.response.PaymentsListRP;

import java.util.Locale;

public class PaymentsRVAdapter extends RecyclerView.Adapter<PaymentsRVAdapter.AssignmentHolder> {
    PaymentsListRP paymentsListRP;
    Context context;

    public PaymentsRVAdapter(PaymentsListRP paymentsListRP, Context context) {
        this.paymentsListRP = paymentsListRP;
        this.context = context;
    }


    @NonNull
    @Override
    public AssignmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AssignmentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_assignments
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentHolder holder, int position) {
        PaymentModel paymentModel = paymentsListRP.getPayHistory().get(position);

        if (paymentModel.getPayment_type().equals("lesson")) {
            if (paymentModel.getLesson_title() != null
                    && !paymentModel.getLesson_title().isEmpty())
                holder.titleTxt.setText(paymentModel.getLesson_title());
            else
                holder.titleTxt.setText(paymentModel.getLesson_id());

            if (paymentModel.getUnit_title() != null
                    && !paymentModel.getUnit_title().isEmpty())
                holder.bodyTxt.setText(paymentModel.getUnit_title() + " Unit" + "\n");
            else
                holder.bodyTxt.setText("Unit " + paymentModel.getUnit_id() + "\n");



        } else {
            if (paymentModel.getEvent_title() != null
                    && !paymentModel.getEvent_title().isEmpty())
                holder.titleTxt.setText(paymentModel.getEvent_title());
            else
                holder.titleTxt.setText(paymentModel.getEvent_id());

            holder.bodyTxt.setText("");
        }

        String status = paymentModel.getStatus();
        int drawableRes = -1;
        if (status.equals("pending"))
            drawableRes = R.drawable.ic_baseline_assignment_24;
        else if (status.equals("success"))
            drawableRes = R.drawable.ic_baseline_assignment_accepted;
        else if (status.equals("failed"))
            drawableRes = R.drawable.ic_baseline_assignment_rejected;

        holder.statusImg.setImageDrawable(context.getDrawable(drawableRes));

        holder.statusTxt.setText(paymentModel.getCurrency() + paymentModel.getAmount());
        holder.actionTxt.setText(status.substring(0,1).toUpperCase(Locale.ROOT) + status.substring(1));

        String orderId = "OrderID: " + paymentModel.getOrder_id();
        holder.bodyTxt.setText(holder.bodyTxt.getText()+orderId);
    }

    @Override
    public int getItemCount() {
        if (paymentsListRP != null && paymentsListRP.getPayHistory() != null){
            return paymentsListRP.getPayHistory().size();
        }
        return 0;
    }

    public class AssignmentHolder extends RecyclerView.ViewHolder{

        TextView titleTxt;
        TextView bodyTxt;
        TextView statusTxt;
        TextView actionTxt;
        ImageView statusImg;

        public AssignmentHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.title);
            bodyTxt = itemView.findViewById(R.id.body);
            statusTxt = itemView.findViewById(R.id.status);
            statusImg = itemView.findViewById(R.id.statusImg);
            actionTxt = itemView.findViewById(R.id.actionTxt);

        }
    }

}

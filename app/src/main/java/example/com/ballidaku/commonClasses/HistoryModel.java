package example.com.ballidaku.commonClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryModel
{

    @SerializedName("listReportTicketCollectionModel")
    @Expose
    private List<ListReportTicketCollectionModel> listReportTicketCollectionModel = null;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Message")
    @Expose
    private String message;

    public List<ListReportTicketCollectionModel> getListReportTicketCollectionModel()
    {
        return listReportTicketCollectionModel;
    }

    public void setListReportTicketCollectionModel(List<ListReportTicketCollectionModel> listReportTicketCollectionModel)
    {
        this.listReportTicketCollectionModel = listReportTicketCollectionModel;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }


    public class ListReportTicketCollectionModel
    {

        @SerializedName("totalTicket")
        @Expose
        private Integer totalTicket;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("totalAmount")
        @Expose
        private Integer totalAmount;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;

        public Integer getTotalTicket()
        {
            return totalTicket;
        }

        public void setTotalTicket(Integer totalTicket)
        {
            this.totalTicket = totalTicket;
        }

        public Integer getTotalCount()
        {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount)
        {
            this.totalCount = totalCount;
        }

        public Integer getTotalAmount()
        {
            return totalAmount;
        }

        public void setTotalAmount(Integer totalAmount)
        {
            this.totalAmount = totalAmount;
        }

        public String getCreatedDate()
        {
            return createdDate;
        }

        public void setCreatedDate(String createdDate)
        {
            this.createdDate = createdDate;
        }

    }
}

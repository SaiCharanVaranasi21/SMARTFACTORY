public class Machine {
    private String id;
    private String productionLine;
    private String productionCode;
    private String status;
    private String fault;
    private String report;

    public Machine(String id, String productionLine, String productionCode,
                   String status, String fault, String report) {
        this.id = id;
        this.productionLine = productionLine;
        this.productionCode = productionCode;
        this.status = status;
        this.fault = fault;
        this.report = report;
    }

    public String getId() {
        return id;
    }

    public void setProductionLine(String productionLine) {
        this.productionLine = productionLine;
    }

    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getProductionLine() {
        return productionLine;
    }

    public String getProductionCode() {
        return productionCode;
    }

    public String getStatus() {
        return status;
    }

    public String getFault() {
        return fault;
    }

    public String getReport() {
        return report;
    }
}

package lg.phantomjs.position.model;

/**
 * Created by xinszhou on 25/12/2016.
 */
public class Position {
    String companyId;
    String positionId;
    String jobNature;
    String financeStage;
    String companyName;
    String companyFullName;
    String companySize;
    String industryField;
    String positionName;
    String city;
//    String createTime;
    String salary;
    String workYear;
    String education;
    String district;

    int minSalary;
    int maxSalary;
    int midSalary;


    public void wrapUp() {

        try {
            String[] parts = salary.split("-");

            if (parts.length < 2) {
                midSalary = Integer.valueOf(salary.split("[Kk]")[0].trim());
                minSalary = midSalary; maxSalary = midSalary;
            } else {
                minSalary = Integer.valueOf(parts[0].split("[Kk]")[0].trim());
                maxSalary = Integer.valueOf(parts[1].split("[Kk]")[0].trim());
                midSalary = (maxSalary + minSalary) / 2;
            }
        } catch (Exception e) {
            System.out.println("positionId: " + positionId + ", company id: " + companyId);
            e.printStackTrace();
        }

    }

    public void setMinSalary(int minSalary) {
        this.minSalary = minSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public void setMidSalary(int midSalary) {
        this.midSalary = midSalary;
    }

    public int getMinSalary() {
        return minSalary;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public int getMidSalary() {
        return midSalary;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public void setJobNature(String jobNature) {
        this.jobNature = jobNature;
    }

    public void setFinanceStage(String financeStage) {
        this.financeStage = financeStage;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyFullName(String companyFullName) {
        this.companyFullName = companyFullName;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public void setIndustryField(String industryField) {
        this.industryField = industryField;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setCity(String city) {
        this.city = city;
    }

//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
////    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getPositionId() {
        return positionId;
    }

    public String getJobNature() {
        return jobNature;
    }

    public String getFinanceStage() {
        return financeStage;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyFullName() {
        return companyFullName;
    }

    public String getCompanySize() {
        return companySize;
    }

    public String getIndustryField() {
        return industryField;
    }

    public String getPositionName() {
        return positionName;
    }

    public String getCity() {
        return city;
    }

//    public String getCreateTime() {
//        return createTime;
//    }

    public String getSalary() {
        return salary;
    }

    public String getWorkYear() {
        return workYear;
    }

    public String getEducation() {
        return education;
    }

    public String getDistrict() {
        return district;
    }
}

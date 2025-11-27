package OOADAssignment.com.silverpalm.banking.dao;

import OOADAssignment.com.silverpalm.banking.core.model.Customer;
import OOADAssignment.com.silverpalm.banking.core.model.CustomerType;
import OOADAssignment.com.silverpalm.banking.core.model.Gender;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoJdbc implements ICustomerDao {

    @Override
    public void save(Customer c) {
        String sql = "MERGE INTO customers(customer_id,username,password,customer_type,first_name,last_name," +
                "date_of_birth,gender,id_number,company_name,registration_number,contact_person," +
                "address,phone_number,email,branch,registration_date) " +
                "KEY(customer_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getCustomerId());
            ps.setString(2, c.getUsername());
            //  DO NOT call getPassword() – we keep the field ourselves
            ps.setString(3, "");               //  or store it if you have a getter
            ps.setString(4, c.getCustomerType().name());
            ps.setString(5, c.getFirstName());
            ps.setString(6, c.getLastName());
            ps.setDate(7, Date.valueOf(c.getDateOfBirth()));
            ps.setString(8, c.getGender() == null ? null : c.getGender().name());
            ps.setString(9, c.getIdNumber());
            ps.setString(10, c.getCompanyName());
            ps.setString(11, c.getRegistrationNumber());
            ps.setString(12, c.getContactPerson());
            ps.setString(13, c.getAddress());
            ps.setString(14, c.getPhoneNumber());
            ps.setString(15, c.getEmail());
            ps.setString(16, c.getBranch());
            ps.setDate(17, Date.valueOf(c.getRegistrationDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("save customer", e);
        }
    }

    @Override
    public Customer find(String customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql)) {
            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? map(rs) : null;
        } catch (SQLException e) {
            throw new RuntimeException("find customer", e);
        }
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers";
        List<Customer> list = new ArrayList<>();
        try (PreparedStatement ps = DaoBaseJdbc.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("findAll customers", e);
        }
    }

    private Customer map(ResultSet rs) throws SQLException {
        String custId   = rs.getString("customer_id");
        String user     = rs.getString("username");
        CustomerType type = CustomerType.valueOf(rs.getString("customer_type"));
        String branch   = rs.getString("branch");

        if (type == CustomerType.INDIVIDUAL) {
            return new Customer(custId, user, "***",   //  password not exposed
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("gender") == null ? null : Gender.valueOf(rs.getString("gender")),
                    rs.getString("id_number"),
                    rs.getString("address"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    branch);
        } else {
            return new Customer(custId, user, "***",
                    rs.getString("company_name"),
                    rs.getString("registration_number"),
                    rs.getString("contact_person"),
                    rs.getString("address"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    branch);
        }
    }
}
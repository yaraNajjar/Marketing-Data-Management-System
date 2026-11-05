import React, { useState, useEffect, useRef } from "react";
import { Button } from "primereact/button";
import { Dialog } from "primereact/dialog";
import { Toast } from "primereact/toast";
import Table from "./Table";
import UserDialog from "../shared/UserDialog";
import { useFetchWithAuth } from "../utils/useFetchWithAuth";
import { rolesFormatHeader } from "../shared/globalFunctions";

const UsersList = () => {
  const fetchWithAuth = useFetchWithAuth();
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [dialogVisible, setDialogVisible] = useState(false);
  const [deleteDialogVisible, setDeleteDialogVisible] = useState(false);
  const [isDialogNewUser, setIsDialogNewUser] = useState(false);
  const toast = useRef(null);

  const roleColumns = [
    "ROLE_CREATE_ACTION",
    "ROLE_UPDATE_ACTION",
    "ROLE_DELETE_ACTION",
    "ROLE_CREATE_METRIC",
    "ROLE_UPDATE_METRIC",
    "ROLE_DELETE_METRIC",
    "ROLE_TRIGGER",
  ];

  const defaultRoles = roleColumns.reduce(
    (acc, role) => ({ ...acc, [role]: false }),
    {}
  );

  const mapRolesFromDb = (rolesArray) => {
    if (!Array.isArray(rolesArray)) return defaultRoles;

    const rolesSet = new Set(rolesArray.map((roleObj) => roleObj.role));
    return roleColumns.reduce((acc, role) => {
      acc[role] = rolesSet.has(role);
      return acc;
    }, {});
  };

  const mapRolesToDb = (rolesObj) => {
    return Object.entries(rolesObj)
      .filter(([, value]) => value)
      .map(([key]) => key);
  };

  const fetchUsers = async () => {
    const response = await fetchWithAuth("http://localhost:8080/users");
    if (response.ok) {
      const users = await response.json();
      const mappedUsers = users.map((user) => ({
        ...user,
        roles: mapRolesFromDb(user.roles),
      }));
      setUsers(mappedUsers);
    } else {
      toast.current.show({
        severity: "error",
        summary: "Fetch Failed",
        detail: `Failed to fetch users.`,
      });
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const openNewUserDialog = () => {
    setSelectedUser({
      id: 0,
      username: "",
      email: "",
      password: "12345",
      roles: defaultRoles,
    });
    setIsDialogNewUser(true);
    setDialogVisible(true);
  };

  const handleInputChange = (field, value) => {
    setSelectedUser((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const saveUser = async () => {
    const { role, ...userToSaveWithoutRole } = selectedUser;
    const userToSave = {
      ...userToSaveWithoutRole,
      password: "12345",
      roles: Array.from(
        new Set([...mapRolesToDb(userToSaveWithoutRole.roles), "ROLE_USER"])
      ),
    };

    const url = isDialogNewUser
      ? `http://localhost:8080/registration`
      : `http://localhost:8080/user/${userToSave.id}`;
    const method = isDialogNewUser ? "POST" : "PUT";

    const response = await fetchWithAuth(url, method, userToSave);
    const resultText = await response.text();

    if (response.ok) {
      fetchUsers();
      toast.current.show({
        severity: "success",
        summary: "User Saved",
        detail: resultText,
      });
    } else {
      toast.current.show({
        severity: "error",
        summary: "Save Failed",
        detail: `Failed to save user: ${resultText}`,
      });
    }

    setDialogVisible(false);
  };

  const onEditUser = (userData) => {
    setSelectedUser(userData);
    setIsDialogNewUser(false);
    setDialogVisible(true);
  };

  const onConfirmDelete = (userData) => {
    setSelectedUser(userData);
    setDeleteDialogVisible(true);
  };

  const deleteUser = async () => {
    const response = await fetchWithAuth(
      `http://localhost:8080/user/${selectedUser.id}`,
      "DELETE"
    );

    if (response.ok) {
      fetchUsers();
      toast.current.show({
        severity: "info",
        summary: "User Deleted",
        detail: "The user has been deleted successfully.",
      });
    } else {
      toast.current.show({
        severity: "error",
        summary: "Deletion Failed",
        detail: `Failed to delete the user.`,
      });
    }

    setDeleteDialogVisible(false);
    setSelectedUser(null);
  };

  const updateUserRoles = (roleKey, value) => {
    setSelectedUser((prev) => ({
      ...prev,
      roles: {
        ...prev.roles,
        [roleKey]: value,
      },
    }));
  };

  const roleBodyTemplate = (role) => (rowData) => {
    return rowData.roles[role] ? (
      <i className="pi pi-plus-circle" style={{ color: "green" }} />
    ) : (
      <i className="pi pi-times-circle" style={{ color: "red" }} />
    );
  };

  const columns = [
    {
      field: "id",
      header: "ID",
      body: (rowData) => rowData.id,
      sortable: true,
    },
    {
      field: "username",
      header: "User Name",
      body: (rowData) => rowData.username,
      sortable: true,
    },
    {
      field: "email",
      header: "Email",
      body: (rowData) => rowData.email,
      sortable: true,
    },
    ...roleColumns.map((role) => ({
      field: role,
      header: rolesFormatHeader(role),
      body: roleBodyTemplate(role),
      sortable: false,
    })),
    {
      field: "actions",
      header: "Actions",
      body: (rowData) => (
        <React.Fragment>
          <Button
            icon="pi pi-pencil"
            className="p-button-rounded p-button-success mr-2"
            onClick={() => onEditUser(rowData)}
          />
          <Button
            icon="pi pi-trash"
            className="p-button-rounded p-button-danger"
            onClick={() => onConfirmDelete(rowData)}
          />
        </React.Fragment>
      ),
    },
  ];

  const dialogFooter = (
    <React.Fragment>
      <Button
        label="Save"
        icon="pi pi-check"
        className="p-button-text"
        onClick={saveUser}
      />
      <Button
        label="Cancel"
        icon="pi pi-times"
        className="p-button-text"
        onClick={() => setDialogVisible(false)}
      />
    </React.Fragment>
  );

  const deleteDialogFooter = (
    <React.Fragment>
      <Button
        label="Yes"
        icon="pi pi-check"
        className="p-button-text"
        onClick={deleteUser}
      />
      <Button
        label="No"
        icon="pi pi-times"
        className="p-button-text"
        onClick={() => setDeleteDialogVisible(false)}
      />
    </React.Fragment>
  );

  const actionButtons = (
    <Button
      label="Add New User"
      icon="pi pi-plus"
      onClick={openNewUserDialog}
      className="p-mb-3 p-button-success"
    />
  );

  return (
    <div>
      <Toast ref={toast} />
      <Dialog
        visible={dialogVisible}
        onHide={() => setDialogVisible(false)}
        footer={dialogFooter}
        style={{ width: "500px" }}
        header="Edit User"
      >
        {selectedUser && (
          <UserDialog
            user={selectedUser}
            visible={dialogVisible}
            onHide={() => setDialogVisible(false)}
            onSave={saveUser}
            onInputChange={handleInputChange}
            updateUserRoles={updateUserRoles}
          />
        )}
      </Dialog>
      <Dialog
        visible={deleteDialogVisible}
        onHide={() => setDeleteDialogVisible(false)}
        footer={deleteDialogFooter}
        style={{ width: "450px" }}
        header="Confirm Delete"
      >
        <p>Are you sure you want to delete this user?</p>
      </Dialog>
      <Table
        tableName="Users"
        data={users}
        actionButtons={actionButtons}
        columns={columns}
        paginator
        rows={5}
      />
    </div>
  );
};

export default UsersList;

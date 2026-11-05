import React from "react";
import { Dialog } from "primereact/dialog";
import { InputSwitch } from "primereact/inputswitch";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { Divider } from "primereact/divider";
import { rolesFormatHeader } from "../shared/globalFunctions";

const UserDialog = ({
  user,
  visible,
  onHide,
  onSave,
  onInputChange,
  updateUserRoles,
  isNew,
}) => {
  const dialogFooter = (
    <React.Fragment>
      <Button
        label="Save"
        icon="pi pi-check"
        className="p-button-text"
        onClick={onSave}
      />
      <Button
        label="Cancel"
        icon="pi pi-times"
        className="p-button-text"
        onClick={onHide}
      />
    </React.Fragment>
  );

  const roleKeys = [
    "ROLE_CREATE_ACTION",
    "ROLE_UPDATE_ACTION",
    "ROLE_DELETE_ACTION",
    "ROLE_CREATE_METRIC",
    "ROLE_UPDATE_METRIC",
    "ROLE_DELETE_METRIC",
    "ROLE_TRIGGER",
  ];

  return (
    <Dialog
      visible={visible}
      onHide={onHide}
      footer={dialogFooter}
      style={{ width: "500px" }}
      header={isNew ? "Add New User" : "Edit User"}
    >
      {user && (
        <div className="p-grid p-fluid">
          <div className="p-col-12 p-md-6">
            <div className="p-field">
              <label htmlFor="username">Username</label>
              <InputText
                id="username"
                value={user.username}
                onChange={(e) => onInputChange("username", e.target.value)}
              />
            </div>
            <div className="p-field">
              <label htmlFor="email">Email</label>
              <InputText
                id="email"
                value={user.email}
                onChange={(e) => onInputChange("email", e.target.value)}
              />
            </div>
          </div>
          <div className="p-col-12 p-md-6">
            <Divider align="center" type="dashed">
              <b>Roles</b>
            </Divider>
            {roleKeys.map((key) => (
              <div key={key} className="p-field-checkbox">
                <InputSwitch
                  inputId={key}
                  checked={user.roles[key]}
                  onChange={(e) => updateUserRoles(key, e.value)}
                />
                <label htmlFor={key} className="p-ml-2">
                  {rolesFormatHeader(key)}
                </label>
              </div>
            ))}
          </div>
        </div>
      )}
    </Dialog>
  );
};

export default UserDialog;

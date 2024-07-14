import React, { useState } from "react";
import useValidatePassword from "../../hook/user/useValidatePassword";
import Modal from "../../component/Modal";
import Button from "../../component/Button";
import Input from "../../component/Input";
import { validatePasswordFormat } from "../../util/passwordUtils";
import useChangePassword from "../../hook/user/useChangePassword";

interface Errors {
  oldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

const initialErrors: Errors = {
  oldPassword: "",
  newPassword: "",
  confirmNewPassword: "",
};

const ChangePasswordModal = () => {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");
  const [stage, setStage] = useState<1 | 2>(1);
  const { validatePassword, validating } = useValidatePassword();
  const { changePassword, changing } = useChangePassword();
  const [errors, setErrors] = useState<Errors>({ ...initialErrors });
  const [open, setOpen] = useState(false);
  const reset = () => {
    setErrors({ ...initialErrors });
    setOldPassword("");
    setNewPassword("");
    setConfirmNewPassword("");
    setStage(1);
  };
  const onNext = () => {
    setErrors({ ...initialErrors });
    if (!oldPassword) {
      setError("oldPassword", "Required");
      return;
    }
    validatePassword(
      { password: oldPassword },
      {
        onSuccess: () => {
          setStage(2);
        },
        onError: (err) => {
          if (err instanceof Error) {
            setError("oldPassword", err.message);
          } else {
            setError("oldPassword", "Failed to validate your password.");
          }
        },
      }
    );
  };

  const setError = (key: keyof Errors, value: string) => {
    setErrors((prev) => {
      const newState = { ...prev };
      newState[key] = value;
      return newState;
    });
  };

  const onClose = () => {
    setOpen(false);
    reset();
  };

  const onSubmit = () => {
    setErrors({ ...initialErrors });
    let valid = true;
    if (!newPassword) {
      setError("newPassword", "Required");
      valid = false;
    }
    try {
      validatePasswordFormat(newPassword);
    } catch (err) {
      if (err instanceof Error) {
        valid = false;
        setError("newPassword", err.message);
      }
    }
    if (!confirmNewPassword) {
      setError("confirmNewPassword", "Required");
      valid = false;
    }
    if (!valid) return;
    if (confirmNewPassword !== newPassword) {
      setError("confirmNewPassword", "Does not match with new password.");
      valid = false;
    }
    if (!valid) return;
    changePassword(
      { password: newPassword },
      {
        onSuccess: () => {
          onClose();
        },
        onError: (err) => {
          if (err instanceof Error) {
            setError("newPassword", err.message);
          }
        },
      }
    );
  };

  return (
    <Modal
      triggerFullWidth
      open={open}
      onOpen={() => setOpen(true)}
      onClose={onClose}
      trigger={
        <Button fullWidth type="outline" color="red" text="Change Password" />
      }
      title="Change Password"
    >
      <div className="flex flex-col gap-[10px] items-end">
        {stage === 1 && (
          <>
            <Input
              title="Old Password"
              type="text"
              masked
              error={errors.oldPassword}
              value={oldPassword}
              onChange={setOldPassword}
            />
            <Button
              type="outline"
              size="sm"
              text="Next"
              loading={validating}
              onClick={onNext}
            />
          </>
        )}
        {stage === 2 && (
          <>
            <Input
              title="New Password"
              type="text"
              masked
              error={errors.newPassword}
              value={newPassword}
              onChange={setNewPassword}
            />
            <Input
              title="Confirm New Password"
              type="text"
              masked
              error={errors.confirmNewPassword}
              value={confirmNewPassword}
              onChange={setConfirmNewPassword}
            />
            <Button
              type="outline"
              size="sm"
              text="Submit"
              loading={changing}
              onClick={onSubmit}
            />
          </>
        )}
      </div>
    </Modal>
  );
};

export default ChangePasswordModal;

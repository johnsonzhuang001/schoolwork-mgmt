import React, { ReactNode, useState } from "react";
import { Modal as MuiModal } from "@mui/material";
import Text from "./Text";
import { AiOutlineClose } from "react-icons/ai";

interface ModalProps {
  open?: boolean;
  onOpen?: (open: boolean) => void;
  onClose?: () => void;
  trigger: ReactNode;
  title: string;
  subtitle?: string;
  children: ReactNode;
  showClose?: boolean;
  maxHeight?: number;
  minHeight?: number;
  triggerFullWidth?: boolean;
}

const Modal: React.FC<ModalProps> = ({
  title,
  subtitle,
  trigger,
  open,
  onOpen,
  onClose,
  children,
  showClose = true,
  maxHeight,
  minHeight,
  triggerFullWidth = false,
}) => {
  const [isOpen, setIsOpen] = useState(false);

  const openModal = () => {
    if (open !== undefined && onOpen !== undefined) {
      onOpen(true);
    } else {
      setIsOpen(true);
    }
  };

  const closeModal = () => {
    if (open !== undefined && onClose !== undefined) {
      onClose();
    } else {
      setIsOpen(false);
    }
  };

  return (
    <div className={triggerFullWidth ? "w-full" : ""}>
      <div className={triggerFullWidth ? "w-full" : ""} onClick={openModal}>
        {trigger}
      </div>
      <MuiModal open={open ?? isOpen}>
        <div className="w-full h-full mobile:p-[30px] p-[15px] flex justify-center items-center">
          <div
            className="sm:w-[600px] w-full flex flex-col gap-[10px] p-[10px] rounded-[10px] bg-white"
            style={{
              maxHeight: maxHeight ?? "100%",
              minHeight,
            }}
          >
            <div className="flex justify-between items-center">
              <div className="flex items-center gap-[5px] overflow-hidden">
                <Text type="secondary" size="sm" className="whitespace-nowrap">
                  {title}
                </Text>
                {subtitle && (
                  <div className="flex items-center overflow-hidden">
                    <Text type="secondary" size="sm">
                      (
                    </Text>
                    <Text
                      type="secondary"
                      size="sm"
                      className="whitespace-nowrap overflow-hidden text-ellipsis"
                    >
                      {subtitle}
                    </Text>
                    <Text type="secondary" size="sm">
                      )
                    </Text>
                  </div>
                )}
              </div>
              {showClose && (
                <Text type="secondary" size="lg" pointer onClick={closeModal}>
                  <AiOutlineClose />
                </Text>
              )}
            </div>
            <div className="w-full h-[1px] bg-whitegray" />
            <div className="grow overflow-auto">{children}</div>
          </div>
        </div>
      </MuiModal>
    </div>
  );
};

export default Modal;

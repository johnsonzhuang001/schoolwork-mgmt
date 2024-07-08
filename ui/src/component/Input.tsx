import React from "react";
import Text from "./Text";

interface InputProps {
  title?: string;
  label?: string;
  placeholder?: string;
  masked?: boolean;
  error?: string;
  inputClassName?: string;
  onFocus?: () => void;
  onBlur?: () => void;
}

interface TextInputProps extends InputProps {
  type: "text" | "textarea";
  value: string;
  onChange: (value: string) => void;
}

interface NumberInputProps extends InputProps {
  type: "number";
  value: number;
  onChange: (value: number) => void;
}

const Input: React.FC<TextInputProps | NumberInputProps> = ({
  type,
  label,
  title,
  value,
  onChange,
  onFocus,
  onBlur,
  placeholder,
  masked = false,
  error,
  inputClassName = "",
}) => {
  const className = `grow px-[8px] bg-transparent sm:text-sm text-xs text-primary outline-0 transition-height duration-200 ${inputClassName}`;
  return (
    <div className="input w-full flex flex-col items-start">
      {title && (
        <div>
          <Text size="xs" type="secondary">
            {title}
          </Text>
        </div>
      )}
      <div className="w-full py-[8px] flex rounded-[6px] bg-whitegray">
        {label && (
          <>
            <div className="px-[8px]">
              <Text type="secondary">{label}</Text>
            </div>
            <div className="w-[1px] h-full bg-secondary" />
          </>
        )}
        {type === "text" && (
          <input
            className={className}
            onFocus={onFocus}
            onBlur={onBlur}
            type={masked ? "password" : "text"}
            value={value}
            placeholder={placeholder ?? title}
            onChange={(evt) => {
              onChange(evt.target.value);
            }}
          />
        )}
        {type === "number" && (
          <input
            className={className}
            onFocus={onFocus}
            onBlur={onBlur}
            type="number"
            value={value}
            placeholder={placeholder ?? title}
            onChange={(evt) => {
              onChange(Number(evt.target.value));
            }}
          />
        )}
        {type === "textarea" && (
          <textarea
            className={className}
            onFocus={onFocus}
            onBlur={onBlur}
            value={value}
            placeholder={placeholder ?? title}
            onChange={(evt) => {
              onChange(evt.target.value);
            }}
          />
        )}
      </div>
      {error && (
        <Text type="danger" size="xs">
          {error}
        </Text>
      )}
    </div>
  );
};

export default Input;

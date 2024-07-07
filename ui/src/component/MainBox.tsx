import { ReactNode } from "react";

const MainBox = ({
  children,
  className,
}: {
  children: ReactNode;
  className?: string;
}) => {
  return (
    <div
      className={`${className} max-w-[1260px] max-h-full mobile:mx-[30px] mx-[10px] min-w-0 w-full h-fit`}
    >
      {children}
    </div>
  );
};

export default MainBox;

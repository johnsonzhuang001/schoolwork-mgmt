import type { Config } from "tailwindcss";
const colors = require("tailwindcss/colors");

const config: Config = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      aspectRatio: {
        "3/4": "3 / 4",
        "4/3": "4 / 3",
        "1/1": "1 / 1",
      },
    },
    screens: {
      mobile: "450px",
      sm: "748px",
      md: "980px",
      lg: "1200px",
    },
    colors: {
      ...colors,
      dark: "#111111",
      lightdark: "#222222",
      primary: "#444444",
      secondary: "#888888",
      green: "#34a853",
      blue: "#4285f4",
      red: "#ea4335",
      orange: "#FF9F47",
      whitegray: "#EEEEEE",
      yellow: "#fbbc05",
      almostwhite: "#F8F8F8",
    },
  },
  plugins: [],
};

export default config;

/** @type {import('tailwindcss').Config} */
const colors = require("tailwindcss/colors");

module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      boxShadow: {
        header: "0 0px 8px 5px rgba(0, 0, 0, 0.05)",
        popup: "0 0px 3px 3px rgba(0, 0, 0, 0.05)",
        card: "0 0px 5px 5px rgba(0, 0, 0, 0.03)",
        light: "0 0px 5px 3px rgba(0, 0, 0, 0.03)",
      },
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
      primary: "#444444",
      secondary: "#888888",
      blue: "#5383FF",
      red: "#C54B4B",
      orange: "#FF9F47",
      whitegray: "#EEEEEE",
      yellow: "#FFD770",
      almostwhite: "#F8F8F8",
    },
  },
  plugins: [],
};

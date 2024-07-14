const regex =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@.#$!%*?&^])[A-Za-z\d@.#$!%*?&]{8,15}$/;

export const isPasswordFormatValid = (password: string): boolean =>
  regex.test(password);

export const validatePasswordFormat = (password: string) => {
  if (!isPasswordFormatValid(password)) {
    throw Error(
      "Password should contain at least one lowercase and uppercase alphabet, one number, one special character @.#$!%*?&^, and with length between 8 and 15."
    );
  }
};

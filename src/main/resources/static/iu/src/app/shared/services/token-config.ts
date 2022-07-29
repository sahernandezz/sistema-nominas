const getToken = () => {
  return localStorage.getItem('USER_KEY');
};

export default getToken;

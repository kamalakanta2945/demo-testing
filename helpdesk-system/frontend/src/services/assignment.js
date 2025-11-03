import api from './api';

export const getAgentLoad = () => {
    return api.get('/assignments/load');
};

import api from './api';

export const createTicket = (ticketData) => {
    return api.post('/tickets', ticketData);
};

export const getAllTickets = () => {
    return api.get('/tickets');
};

export const getTicketById = (id) => {
    return api.get(`/tickets/${id}`);
};

export const addReply = (id, replyData) => {
    return api.post(`/tickets/${id}/replies`, replyData);
};

export const getReplies = (id) => {
    return api.get(`/tickets/${id}/replies`);
};

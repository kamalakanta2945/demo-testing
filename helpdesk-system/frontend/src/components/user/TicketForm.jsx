import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createTicket } from '../../services/ticket';

const TicketForm = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [department, setDepartment] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();
        createTicket({ title, description, department }).then(() => {
            navigate('/dashboard');
        });
    };

    return (
        <form onSubmit={handleSubmit}>
            <h2>Create a Ticket</h2>
            <input
                type="text"
                placeholder="Title"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
            />
            <textarea
                placeholder="Description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />
            <input
                type="text"
                placeholder="Department"
                value={department}
                onChange={(e) => setDepartment(e.target.value)}
            />
            <button type="submit">Create</button>
        </form>
    );
};

export default TicketForm;

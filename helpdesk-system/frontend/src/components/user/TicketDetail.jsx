import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getTicketById, addReply, getReplies } from '../../services/ticket';

const TicketDetail = () => {
    const { id } = useParams();
    const [ticket, setTicket] = useState(null);
    const [replies, setReplies] = useState([]);
    const [reply, setReply] = useState('');

    useEffect(() => {
        getTicketById(id).then((response) => {
            setTicket(response.data);
        });
        getReplies(id).then((response) => {
            setReplies(response.data);
        });
    }, [id]);

    const handleSubmit = (e) => {
        e.preventDefault();
        addReply(id, { content: reply }).then(() => {
            setReply('');
            getReplies(id).then((response) => {
                setReplies(response.data);
            });
        });
    };

    if (!ticket) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>{ticket.title}</h2>
            <p>{ticket.description}</p>
            <p>Status: {ticket.status}</p>
            <p>Department: {ticket.department}</p>
            <p>Assigned to: {ticket.assignedTo}</p>

            <h3>Replies</h3>
            <ul>
                {replies.map((r) => (
                    <li key={r.id}>
                        <p>{r.content}</p>
                        <p>By: {r.createdBy} at {new Date(r.createdAt).toLocaleString()}</p>
                    </li>
                ))}
            </ul>

            <form onSubmit={handleSubmit}>
                <textarea
                    placeholder="Add a reply"
                    value={reply}
                    onChange={(e) => setReply(e.target.value)}
                />
                <button type="submit">Reply</button>
            </form>
        </div>
    );
};

export default TicketDetail;

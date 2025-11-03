import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createTicket } from '../../services/ticket';
import { Button, TextField, Typography, Container, Paper } from '@mui/material';

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
        <Container component="main" maxWidth="sm">
            <Paper elevation={3} style={{ padding: 20 }}>
                <Typography component="h1" variant="h5">
                    Create a Ticket
                </Typography>
                <form onSubmit={handleSubmit} style={{ marginTop: 1 }}>
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="title"
                        label="Title"
                        name="title"
                        autoFocus
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        id="department"
                        label="Department"
                        name="department"
                        value={department}
                        onChange={(e) => setDepartment(e.target.value)}
                    />
                    <TextField
                        variant="outlined"
                        margin="normal"
                        required
                        fullWidth
                        name="description"
                        label="Description"
                        id="description"
                        multiline
                        rows={4}
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        style={{ marginTop: 2 }}
                    >
                        Create
                    </Button>
                </form>
            </Paper>
        </Container>
    );
};

export default TicketForm;

import React, { useState } from 'react';
import {
  Box,
  Typography,
  Rating,
  Paper,
  IconButton,
  Menu,
  MenuItem,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Alert,
} from '@mui/material';
import {
  MoreVert,
  Edit,
  Delete,
  Person,
} from '@mui/icons-material';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store';
import { updateReview, deleteReview } from '../../store/slices/reviewSlice';
import { Review, ReviewUpdateRequest } from '../../types';

interface ReviewItemProps {
  review: Review;
}

const ReviewItem: React.FC<ReviewItemProps> = ({ review }) => {
  const dispatch = useDispatch();
  const { user } = useSelector((state: RootState) => state.auth);
  const { isSubmitting, error } = useSelector((state: RootState) => state.reviews);

  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [editData, setEditData] = useState({
    rating: review.rating,
    reviewText: review.reviewText,
  });

  const canEdit = user && (user.id === review.userId || user.role === 'ROLE_ADMIN');

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleEditClick = () => {
    setEditData({
      rating: review.rating,
      reviewText: review.reviewText,
    });
    setEditDialogOpen(true);
    handleMenuClose();
  };

  const handleDeleteClick = () => {
    setDeleteDialogOpen(true);
    handleMenuClose();
  };

  const handleEditSubmit = async () => {
    if (editData.reviewText.trim().length < 10) {
      return;
    }

    const updateData: ReviewUpdateRequest = {
      rating: editData.rating,
      reviewText: editData.reviewText.trim(),
    };

    try {
      await dispatch(updateReview({ reviewId: review.id, reviewData: updateData }) as any);
      setEditDialogOpen(false);
    } catch (error) {
      // Error handled by Redux
    }
  };

  const handleDeleteConfirm = async () => {
    try {
      await dispatch(deleteReview(review.id) as any);
      setDeleteDialogOpen(false);
    } catch (error) {
      // Error handled by Redux
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  return (
    <>
      <Paper sx={{ p: 3, mb: 2 }}>
        <Box display="flex" justifyContent="space-between" alignItems="flex-start">
          <Box flex={1}>
            <Box display="flex" alignItems="center" mb={1}>
              <Person sx={{ mr: 1, color: 'text.secondary' }} />
              <Typography variant="subtitle1" fontWeight="bold">
                {review.reviewerName}
              </Typography>
              <Typography variant="caption" color="text.secondary" sx={{ ml: 2 }}>
                {formatDate(review.createdAt)}
              </Typography>
            </Box>

            <Box display="flex" alignItems="center" mb={2}>
              <Rating value={review.rating} readOnly size="small" />
              <Typography variant="body2" sx={{ ml: 1 }}>
                {review.rating}/5
              </Typography>
            </Box>

            <Typography variant="body1" paragraph>
              {review.reviewText}
            </Typography>

            {review.updatedAt !== review.createdAt && (
              <Typography variant="caption" color="text.secondary">
                Edited on {formatDate(review.updatedAt)}
              </Typography>
            )}
          </Box>

          {canEdit && (
            <IconButton onClick={handleMenuOpen}>
              <MoreVert />
            </IconButton>
          )}
        </Box>
      </Paper>

      {/* Menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
      >
        <MenuItem onClick={handleEditClick}>
          <Edit sx={{ mr: 1 }} />
          Edit
        </MenuItem>
        <MenuItem onClick={handleDeleteClick}>
          <Delete sx={{ mr: 1 }} />
          Delete
        </MenuItem>
      </Menu>

      {/* Edit Dialog */}
      <Dialog open={editDialogOpen} onClose={() => setEditDialogOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>Edit Review</DialogTitle>
        <DialogContent>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}
          
          <Box sx={{ mb: 3, mt: 1 }}>
            <Typography component="legend" gutterBottom>
              Rating
            </Typography>
            <Rating
              value={editData.rating}
              onChange={(_, newValue) => setEditData(prev => ({ ...prev, rating: newValue || 0 }))}
              size="large"
            />
          </Box>

          <TextField
            fullWidth
            label="Review Text"
            multiline
            rows={4}
            value={editData.reviewText}
            onChange={(e) => setEditData(prev => ({ ...prev, reviewText: e.target.value }))}
            helperText={`${editData.reviewText.length}/1000 characters`}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEditDialogOpen(false)}>Cancel</Button>
          <Button 
            onClick={handleEditSubmit} 
            variant="contained"
            disabled={isSubmitting || editData.reviewText.trim().length < 10}
          >
            {isSubmitting ? 'Updating...' : 'Update'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Delete Dialog */}
      <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
        <DialogTitle>Delete Review</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this review? This action cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>Cancel</Button>
          <Button 
            onClick={handleDeleteConfirm} 
            color="error" 
            variant="contained"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Deleting...' : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
};

export default ReviewItem;
